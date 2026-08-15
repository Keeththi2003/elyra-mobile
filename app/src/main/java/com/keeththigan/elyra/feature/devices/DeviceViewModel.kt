package com.keeththigan.elyra.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.data.repository.DeviceRepository
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
    val isDeleted: Boolean = false
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


    // ========================================================================
    // LOAD
    // ========================================================================

    fun loadDevices() {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.getDevices()
                .onSuccess { devices ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            devices = devices
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

    fun loadDevice(
        deviceId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null,
                    selectedDevice = null
                )

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
                _state.value.copy(
                    error = "Please enter a device name."
                )
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            val device = Device(
                name = name,
                type = type,
                floorId = floorId,
                roomId = roomId,
                status = status,
                brightness = brightness,
                switchCount = switchCount,
                maxOnDurationMinutes = maxOnDurationMinutes,
                cameraUri = cameraUri
            )

            repository.createDevice(device)
                .onSuccess { created ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            devices = _state.value.devices + created,
                            isSaved = true
                        )
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
        device: Device
    ) {

        if (device.name.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a device name."
                )
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.updateDevice(device)
                .onSuccess { updated ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            devices = _state.value.devices.map {
                                if (it.id == updated.id) updated else it
                            },
                            selectedDevice = updated,
                            isSaved = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to update device."
                        )
                }
        }
    }

    fun toggleDevice(
        deviceId: String,
        isOn: Boolean
    ) {

        val device =
            _state.value.devices.find { it.id == deviceId }
                ?: _state.value.selectedDevice?.takeIf { it.id == deviceId }
                ?: return

        updateDevice(
            device.copy(
                status = if (isOn) DeviceStatus.ON else DeviceStatus.OFF
            )
        )
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    fun deleteDevice(
        deviceId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.deleteDevice(deviceId)
                .onSuccess {

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            devices = _state.value.devices.filterNot {
                                it.id == deviceId
                            },
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
    // ONE-SHOT SIGNAL CONSUMPTION
    // ========================================================================

    fun consumeSaved() {
        _state.value =
            _state.value.copy(isSaved = false)
    }

    fun consumeDeleted() {
        _state.value =
            _state.value.copy(isDeleted = false)
    }

    fun clearError() {
        _state.value =
            _state.value.copy(error = null)
    }
}
