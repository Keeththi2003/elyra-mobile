package com.keeththigan.elyra.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceConnectivity
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.data.model.SwitchChannel
import com.keeththigan.elyra.core.connectivity.NetworkMonitor
import com.keeththigan.elyra.data.model.AppNotification
import com.keeththigan.elyra.data.model.NotificationType
import com.keeththigan.elyra.data.repository.DeviceRepository
import com.keeththigan.elyra.data.repository.NotificationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

class DeviceViewModel(
    private val repository: DeviceRepository,
    private val networkMonitor: NetworkMonitor,
    private val notificationRepository: NotificationRepository
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

    private fun observeConnectivity() {

        _state.value =
            _state.value.copy(isOnline = networkMonitor.currentlyOnline())

        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _state.value = _state.value.copy(isOnline = online)
            }
        }
    }

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

            // Only persist the fields that belong to this device type — an
            // iron has no brightness, a light has no stream URI.
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
     * Applies a control change optimistically, then persists it. Without the
     * local-first step the toggle visibly snaps back until Firestore
     * acknowledges the write. Reverts to [previous] on failure.
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

        // Offline writes only reach the local cache, so the UI would imply a
        // change that never got to the hardware.
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
     * Simulates the gateway reporting a new link state. Real hardware writes
     * this field directly; exposing it here allows demoing without devices.
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

    private fun startSafetyCutoffWorker() {

        viewModelScope.launch {

            while (true) {

                delay(SAFETY_CHECK_INTERVAL_MS)

                // Never cut off while offline: the app would show the appliance
                // as safely off while it is still physically running.
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

                    val message =
                        "${device.name} was switched off automatically " +
                            "after ${device.maxOnDurationMinutes} minutes."

                    repository.updateDevice(
                        device.copy(
                            isOn = false,
                            lastOnAt = null,
                            totalOnSeconds =
                                device.totalOnSeconds + elapsedOnSeconds(device),
                            lastSafetyCutoffAt = Timestamp.now()
                        )
                    )

                    // Persisted so the alert reaches every signed-in client,
                    // not just the one that happened to run the check.
                    notificationRepository.createNotification(
                        AppNotification(
                            type = NotificationType.SAFETY_CUTOFF,
                            title = "Safety cutoff",
                            message = message,
                            deviceId = device.id,
                            deviceName = device.name
                        )
                    )

                    _state.value =
                        _state.value.copy(safetyAlert = message)
                }
            }
        }
    }

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
        // Polled every second so the cutoff lands within ~1s of the limit.
        const val SAFETY_CHECK_INTERVAL_MS = 1_000L
    }
}
