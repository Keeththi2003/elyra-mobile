package com.keeththigan.elyra.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceConnectivity
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.data.model.SwitchChannel
import com.keeththigan.elyra.core.connectivity.NetworkMonitor
import com.keeththigan.elyra.data.repository.DeviceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// ============================================================================
// DEVICE STATE
// ============================================================================

data class DeviceUiState(
    val isLoading: Boolean = false,
    val devices: List<Device> = emptyList(),
    val selectedDevice: Device? = null,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    /** Set when the safety cutoff forced a device off, for a UI alert. */
    val safetyAlert: String? = null,
    /** False when the phone has no usable internet connection. */
    val isOnline: Boolean = true
)


// ============================================================================
// DEVICE VIEW MODEL
// ============================================================================

class DeviceViewModel(
    private val repository: DeviceRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceUiState())

    val state: StateFlow<DeviceUiState> =
        _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeConnectivity()
        observeDevices()
        startSafetyCutoffWorker()
    }


    // ========================================================================
    // CONNECTIVITY
    // ========================================================================

    private fun observeConnectivity() {

        _state.value =
            _state.value.copy(isOnline = networkMonitor.currentlyOnline())

        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _state.value = _state.value.copy(isOnline = online)
            }
        }
    }


    // ========================================================================
    // REALTIME OBSERVATION
    // ========================================================================

    private fun observeDevices() {

        if (observeJob != null) return

        _state.value = _state.value.copy(isLoading = true)

        observeJob = viewModelScope.launch {

            repository.observeDevices().collect { result ->

                result
                    .onSuccess { devices ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                devices = devices,
                                // Keep the open detail screen in sync too.
                                selectedDevice = _state.value.selectedDevice
                                    ?.let { selected ->
                                        devices.find { it.id == selected.id }
                                            ?: selected
                                    }
                            )
                    }
                    .onFailure { exception ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = exception.message
                                    ?: "Failed to load devices."
                            )
                    }
            }
        }
    }

    /** Kept for screens that call it on entry; the stream is already live. */
    fun loadDevices() {
        observeDevices()
    }

    fun loadDevice(
        deviceId: String
    ) {

        // Serve instantly from the live list when we already have it.
        _state.value.devices.find { it.id == deviceId }?.let {
            _state.value = _state.value.copy(selectedDevice = it)
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(isLoading = true, error = null)

            repository.getDevice(deviceId)
                .onSuccess { device ->
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            selectedDevice = device
                        )
                }
                .onFailure { exception ->
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load device."
                        )
                }
        }
    }


    // ========================================================================
    // CREATE
    // ========================================================================

    fun createDevice(
        name: String,
        type: DeviceType,
        floorId: String = "",
        roomId: String = "",
        isOn: Boolean = false,
        brightness: Int? = null,
        switchCount: Int? = null,
        switchNames: List<String> = emptyList(),
        maxOnDurationMinutes: Int? = null,
        cameraUri: String? = null
    ) {

        if (name.isBlank()) {
            _state.value =
                _state.value.copy(error = "Please enter a device name.")
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(isLoading = true, error = null)

            // A multi-switch starts with one addressable channel per gang.
            val channels =
                if (type == DeviceType.MULTI_SWITCH) {
                    val count = (switchCount ?: 2).coerceIn(1, 8)
                    (1..count).map { index ->
                        SwitchChannel(
                            index = index,
                            name = switchNames.getOrNull(index - 1)
                                ?.takeIf { it.isNotBlank() }
                                ?: "Switch $index",
                            isOn = false
                        )
                    }
                } else {
                    emptyList()
                }

            /*
             * Only persist the configuration that belongs to this device
             * type. An iron has no brightness and a light has no stream URI,
             * so writing those fields would put meaningless data on the
             * document and make the model impossible to reason about.
             */
            val device = Device(
                name = name,
                type = type,
                floorId = floorId,
                roomId = roomId,
                isOn = isOn,
                brightness = brightness
                    .takeIf { type == DeviceType.LIGHT },
                switches = channels,
                maxOnDurationMinutes = maxOnDurationMinutes
                    .takeIf { type == DeviceType.SAFETY_APPLIANCE },
                cameraUri = cameraUri
                    .takeIf { type == DeviceType.SECURITY_CAMERA },
                lastOnAt = if (isOn) Timestamp.now() else null
            )

            repository.createDevice(device)
                .onSuccess {
                    _state.value =
                        _state.value.copy(isLoading = false, isSaved = true)
                }
                .onFailure { exception ->
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to create device."
                        )
                }
        }
    }


    // ========================================================================
    // UPDATE
    // ========================================================================

    fun updateDevice(
        device: Device,
        signalSaved: Boolean = true
    ) {

        if (device.name.isBlank()) {
            _state.value =
                _state.value.copy(error = "Please enter a device name.")
            return
        }

        viewModelScope.launch {

            repository.updateDevice(device)
                .onSuccess { updated ->
                    _state.value =
                        _state.value.copy(
                            selectedDevice = updated,
                            isSaved = if (signalSaved) true else _state.value.isSaved
                        )
                }
                .onFailure { exception ->
                    _state.value =
                        _state.value.copy(
                            error = exception.message
                                ?: "Failed to update device."
                        )
                }
        }
    }

    /**
     * Applies a control change to local state immediately, then persists it.
     *
     * Without the optimistic step the switch stays in its old position until
     * Firestore acknowledges the write, which reads as the toggle flicking
     * back off. On failure the previous value is restored and the error shown.
     */
    private fun applyControlChange(
        previous: Device,
        updated: Device
    ) {

        patchLocal(updated)

        viewModelScope.launch {

            repository.updateDevice(updated)
                .onFailure { exception ->

                    patchLocal(previous)

                    _state.value =
                        _state.value.copy(
                            error = exception.message
                                ?: "Failed to update device."
                        )
                }
        }
    }

    private fun patchLocal(
        device: Device
    ) {
        _state.value =
            _state.value.copy(
                devices = _state.value.devices.map {
                    if (it.id == device.id) device else it
                },
                selectedDevice = _state.value.selectedDevice
                    ?.takeIf { it.id == device.id }
                    ?.let { device }
                    ?: _state.value.selectedDevice
            )
    }

    /**
     * Powers a device on or off, stamping the ON time for the safety cutoff
     * and folding the completed run into accumulated usage on the way off.
     */
    fun toggleDevice(
        deviceId: String,
        isOn: Boolean
    ) {

        val device = findDevice(deviceId) ?: return

        // An unreachable device cannot be operated, and neither can any
        // device while the phone itself is offline — Firestore would queue
        // the write locally and the UI would imply a change that never
        // reached the hardware.
        if (!device.isControllable || !_state.value.isOnline) return

        val elapsedSeconds =
            if (!isOn) elapsedOnSeconds(device) else 0L

        applyControlChange(
            previous = device,
            updated = device.copy(
                isOn = isOn,
                // Powering the whole unit drives every channel with it.
                switches = device.switches.map { it.copy(isOn = isOn) },
                lastOnAt = if (isOn) Timestamp.now() else null,
                totalOnSeconds = device.totalOnSeconds + elapsedSeconds
            )
        )
    }

    /** Flips one channel of a multi-switch gang box independently. */
    fun toggleSwitchChannel(
        deviceId: String,
        channelIndex: Int,
        isOn: Boolean
    ) {

        val device = findDevice(deviceId) ?: return

        if (!device.isControllable || !_state.value.isOnline) return

        val updatedChannels =
            device.switches.map { channel ->
                if (channel.index == channelIndex) {
                    channel.copy(isOn = isOn)
                } else {
                    channel
                }
            }

        // The unit reads as ON while any single channel is live.
        val anyOn = updatedChannels.any { it.isOn }

        applyControlChange(
            previous = device,
            updated = device.copy(
                switches = updatedChannels,
                isOn = anyOn,
                lastOnAt = if (anyOn) device.lastOnAt ?: Timestamp.now() else null
            )
        )
    }

    fun setBrightness(
        deviceId: String,
        brightness: Int
    ) {
        val device = findDevice(deviceId) ?: return

        applyControlChange(
            previous = device,
            updated = device.copy(brightness = brightness.coerceIn(0, 100))
        )
    }

    fun setSchedule(
        deviceId: String,
        enabled: Boolean,
        start: String?,
        end: String?
    ) {
        val device = findDevice(deviceId) ?: return

        applyControlChange(
            previous = device,
            updated = device.copy(
                scheduleEnabled = enabled,
                scheduleStart = start,
                scheduleEnd = end
            )
        )
    }

    fun setMaxOnDuration(
        deviceId: String,
        minutes: Int
    ) {
        val device = findDevice(deviceId) ?: return

        applyControlChange(
            previous = device,
            updated = device.copy(maxOnDurationMinutes = minutes.coerceAtLeast(1))
        )
    }

    /**
     * Simulates the device/gateway reporting a new link state.
     *
     * Real hardware (or the companion simulator) would write this field
     * directly in Firestore; exposing it here lets the connectivity states
     * required by the brief be demonstrated without physical devices.
     */
    fun setConnectivity(
        deviceId: String,
        connectivity: DeviceConnectivity
    ) {
        val device = findDevice(deviceId) ?: return

        applyControlChange(
            previous = device,
            updated = device.copy(connectivity = connectivity)
        )
    }

    /** Renames one channel of a multi-switch unit. */
    fun renameSwitchChannel(
        deviceId: String,
        channelIndex: Int,
        name: String
    ) {
        val device = findDevice(deviceId) ?: return

        applyControlChange(
            previous = device,
            updated = device.copy(
                switches = device.switches.map { channel ->
                    if (channel.index == channelIndex) {
                        channel.copy(name = name)
                    } else {
                        channel
                    }
                }
            )
        )
    }


    // ========================================================================
    // SAFETY CUTOFF
    //
    // Watches safety-critical devices and forces them OFF once they exceed
    // their configured maximum ON duration.
    //
    // NOTE: the brief describes this running as a backend listener/worker.
    // Cloud Functions require the Blaze plan, so this project runs the same
    // rule as an in-app worker writing the OFF state back to Firestore, where
    // every other client picks it up through the realtime listeners.
    // ========================================================================

    private fun startSafetyCutoffWorker() {

        viewModelScope.launch {

            while (true) {

                delay(SAFETY_CHECK_INTERVAL_MS)

                /*
                 * Never cut off while offline. The write would only reach the
                 * local cache, so the app would show the appliance as safely
                 * off while it is still physically running.
                 */
                if (!_state.value.isOnline) continue

                val expired =
                    _state.value.devices.filter { device ->
                        device.type == DeviceType.SAFETY_APPLIANCE &&
                            device.isOn &&
                            device.maxOnDurationMinutes != null &&
                            elapsedOnSeconds(device) >=
                            device.maxOnDurationMinutes * 60L
                    }

                expired.forEach { device ->

                    repository.updateDevice(
                        device.copy(
                            isOn = false,
                            lastOnAt = null,
                            totalOnSeconds =
                                device.totalOnSeconds + elapsedOnSeconds(device),
                            lastSafetyCutoffAt = Timestamp.now()
                        )
                    )

                    _state.value =
                        _state.value.copy(
                            safetyAlert =
                                "${device.name} was switched off automatically " +
                                    "after ${device.maxOnDurationMinutes} minutes."
                        )
                }
            }
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    fun deleteDevice(
        deviceId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(isLoading = true, error = null)

            repository.deleteDevice(deviceId)
                .onSuccess {
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            selectedDevice = null,
                            isDeleted = true
                        )
                }
                .onFailure { exception ->
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to delete device."
                        )
                }
        }
    }


    // ========================================================================
    // HELPERS
    // ========================================================================

    private fun findDevice(
        deviceId: String
    ): Device? =
        _state.value.devices.find { it.id == deviceId }
            ?: _state.value.selectedDevice?.takeIf { it.id == deviceId }

    /** Seconds the device has been continuously ON, 0 if it is off. */
    private fun elapsedOnSeconds(
        device: Device
    ): Long {
        val startedAt = device.lastOnAt ?: return 0L
        val elapsed = Timestamp.now().seconds - startedAt.seconds
        return elapsed.coerceAtLeast(0L)
    }


    // ========================================================================
    // ONE-SHOT SIGNAL CONSUMPTION
    // ========================================================================

    fun consumeSaved() {
        _state.value = _state.value.copy(isSaved = false)
    }

    fun consumeDeleted() {
        _state.value = _state.value.copy(isDeleted = false)
    }

    fun consumeSafetyAlert() {
        _state.value = _state.value.copy(safetyAlert = null)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private companion object {
        const val SAFETY_CHECK_INTERVAL_MS = 10_000L
    }
}
