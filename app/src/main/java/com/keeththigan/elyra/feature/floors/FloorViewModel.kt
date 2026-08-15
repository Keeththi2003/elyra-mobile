package com.keeththigan.elyra.feature.floors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeththigan.elyra.data.model.Floor
import com.keeththigan.elyra.data.model.Room
import com.keeththigan.elyra.data.repository.DeviceRepository
import com.keeththigan.elyra.data.repository.FloorRepository
import com.keeththigan.elyra.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


// ============================================================================
// FLOOR STATE
// ============================================================================

data class FloorUiState(
    val isLoading: Boolean = false,
    val floors: List<Floor> = emptyList(),
    val selectedFloor: Floor? = null,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
)


// ============================================================================
// FLOOR VIEW MODEL
// ============================================================================

class FloorViewModel(
    private val floorRepository: FloorRepository,
    private val roomRepository: RoomRepository,
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FloorUiState())

    val state: StateFlow<FloorUiState> =
        _state.asStateFlow()

    private var observeJob: kotlinx.coroutines.Job? = null

    init {
        observeFloors()
    }


    // ========================================================================
    // LOAD (realtime)
    // ========================================================================

    private fun observeFloors() {

        if (observeJob != null) return

        _state.value = _state.value.copy(isLoading = true)

        observeJob = viewModelScope.launch {

            floorRepository.observeFloors().collect { result ->

                result
                    .onSuccess { floors ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                floors = floors,
                                selectedFloor = _state.value.selectedFloor
                                    ?.let { selected ->
                                        floors.find { it.id == selected.id }
                                            ?: selected
                                    }
                            )
                    }
                    .onFailure { exception ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = exception.message
                                    ?: "Failed to load floors."
                            )
                    }
            }
        }
    }

    /** Kept for screens that call it on entry; the stream is already live. */
    fun loadFloors() {
        observeFloors()
    }

    fun loadFloor(
        floorId: String
    ) {

        if (floorId.isBlank()) {
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null,
                    selectedFloor = null
                )

            floorRepository.getFloor(floorId)
                .onSuccess { floor ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            selectedFloor = floor
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load floor."
                        )
                }
        }
    }


    // ========================================================================
    // CREATE
    // ========================================================================

    fun createFloor(
        name: String
    ) {

        if (name.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a floor name."
                )
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            floorRepository.createFloor(Floor(name = name))
                .onSuccess { created ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            floors = _state.value.floors + created,
                            isSaved = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to create floor."
                        )
                }
        }
    }

    /**
     * Creates a floor along with its rooms in one flow (AddFloorScreen),
     * optionally reassigning pre-existing unassigned devices into those
     * rooms. Best-effort: stops and surfaces the error on first failure,
     * without rolling back what already succeeded.
     */
    fun createFloorWithRooms(
        floorName: String,
        rooms: List<Pair<String, List<String>>> // room name -> device IDs to assign
    ) {

        if (floorName.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a floor name."
                )
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            val floorResult =
                floorRepository.createFloor(Floor(name = floorName))

            val floor =
                floorResult.getOrElse { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to create floor."
                        )

                    return@launch
                }

            for ((roomName, deviceIds) in rooms) {

                val roomResult =
                    roomRepository.createRoom(
                        Room(
                            name = roomName,
                            floorId = floor.id
                        )
                    )

                val room =
                    roomResult.getOrElse { exception ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                floors = _state.value.floors + floor,
                                error = exception.message
                                    ?: "Failed to create room \"$roomName\"."
                            )

                        return@launch
                    }

                for (deviceId in deviceIds) {

                    val deviceResult =
                        deviceRepository.getDevice(deviceId)

                    val device =
                        deviceResult.getOrNull()
                            ?: continue

                    deviceRepository.updateDevice(
                        device.copy(
                            floorId = floor.id,
                            roomId = room.id
                        )
                    )
                }
            }

            _state.value =
                _state.value.copy(
                    isLoading = false,
                    floors = _state.value.floors + floor,
                    isSaved = true
                )
        }
    }


    // ========================================================================
    // UPDATE
    // ========================================================================

    fun updateFloor(
        floorId: String,
        name: String
    ) {

        if (name.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a floor name."
                )
            return
        }

        val existing =
            _state.value.floors.find { it.id == floorId }
                ?: _state.value.selectedFloor?.takeIf { it.id == floorId }
                ?: return

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            floorRepository.updateFloor(
                existing.copy(name = name)
            )
                .onSuccess { updated ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            floors = _state.value.floors.map {
                                if (it.id == updated.id) updated else it
                            },
                            selectedFloor = updated,
                            isSaved = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to update floor."
                        )
                }
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    fun deleteFloor(
        floorId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            floorRepository.deleteFloor(floorId)
                .onSuccess {

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            floors = _state.value.floors.filterNot {
                                it.id == floorId
                            },
                            selectedFloor = null,
                            isDeleted = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to delete floor."
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
