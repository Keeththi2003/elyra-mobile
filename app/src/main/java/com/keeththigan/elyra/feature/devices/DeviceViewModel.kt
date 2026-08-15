package com.keeththigan.elyra.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.data.model.SwitchChannel
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
    val safetyAlert: String? = null
)


// ============================================================================
// DEVICE VIEW MODEL
// ============================================================================

class DeviceViewModel(
    private val repository: DeviceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceUiState())

    val state: StateFlow<DeviceUiState> =
        _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeDevices()
        startSafetyCutoffWorker()
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
        status: DeviceStatus = DeviceStatus.OFF,
        brightness: Int? = null,
        switchCount: Int? = null,
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
                    (1..count).map {
                        SwitchChannel(
                            index = it,
                            name = "Switch $it",
                            isOn = false
                        )
                    }
                } else {
                    emptyList()
                }

            val device = Device(
                name = name,
                type = type,
                floorId = floorId,
                roomId = roomId,
                status = status,
                brightness = brightness,
                switchCount = switchCount,
                switches = channels,
                maxOnDurationMinutes = maxOnDurationMinutes,
                cameraUri = cameraUri,
                lastOnAt = if (status == DeviceStatus.ON) Timestamp.now() else null
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
     * Powers a device on or off, stamping the ON time for the safety cutoff
     * and folding the completed run into accumulated usage on the way off.
     */
    fun toggleDevice(
        deviceId: String,
        isOn: Boolean
    ) {

        val device = findDevice(deviceId) ?: return

        val elapsedSeconds =
            if (!isOn) elapsedOnSeconds(device) else 0L

        updateDevice(
            device.copy(
                status = if (isOn) DeviceStatus.ON else DeviceStatus.OFF,
                lastOnAt = if (isOn) Timestamp.now() else null,
                totalOnSeconds = device.totalOnSeconds + elapsedSeconds
            ),
            signalSaved = false
        )
    }

    /** Flips one channel of a multi-switch gang box independently. */
    fun toggleSwitchChannel(
        deviceId: String,
        channelIndex: Int,
        isOn: Boolean
    ) {

        val device = findDevice(deviceId) ?: return

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

        updateDevice(
            device.copy(
                switches = updatedChannels,
                status = if (anyOn) DeviceStatus.ON else DeviceStatus.OFF,
                lastOnAt = if (anyOn) device.lastOnAt ?: Timestamp.now() else null
            ),
            signalSaved = false
        )
    }

    fun setBrightness(
        deviceId: String,
        brightness: Int
    ) {
        val device = findDevice(deviceId) ?: return

        updateDevice(
            device.copy(brightness = brightness.coerceIn(0, 100)),
            signalSaved = false
        )
    }

    fun setSchedule(
        deviceId: String,
        enabled: Boolean,
        start: String?,
        end: String?
    ) {
        val device = findDevice(deviceId) ?: return

        updateDevice(
            device.copy(
                scheduleEnabled = enabled,
                scheduleStart = start,
                scheduleEnd = end
            ),
            signalSaved = false
        )
    }

    fun setMaxOnDuration(
        deviceId: String,
        minutes: Int
    ) {
        val device = findDevice(deviceId) ?: return

        updateDevice(
            device.copy(maxOnDurationMinutes = minutes.coerceAtLeast(1)),
            signalSaved = false
        )
    }

    fun setDeviceStatus(
        deviceId: String,
        status: DeviceStatus
    ) {
        val device = findDevice(deviceId) ?: return

        updateDevice(device.copy(status = status), signalSaved = false)
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

                val expired =
                    _state.value.devices.filter { device ->
                        device.type == DeviceType.SAFETY_APPLIANCE &&
                            device.status == DeviceStatus.ON &&
                            device.maxOnDurationMinutes != null &&
                            elapsedOnSeconds(device) >=
                            device.maxOnDurationMinutes * 60L
                    }

                expired.forEach { device ->

                    repository.updateDevice(
                        device.copy(
                            status = DeviceStatus.OFF,
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
