package com.keeththigan.elyra.feature.floors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keeththigan.elyra.data.model.Room
import com.keeththigan.elyra.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RoomUiState(
    val isLoading: Boolean = false,
    val rooms: List<Room> = emptyList(),
    val selectedRoom: Room? = null,
    val error: String? = null,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false
)

class RoomViewModel(
    private val repository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoomUiState())

    val state: StateFlow<RoomUiState> =
        _state.asStateFlow()

    private var observeJob: kotlinx.coroutines.Job? = null

    init {
        observeRooms()
    }

    private fun observeRooms() {

        if (observeJob != null) return

        _state.value = _state.value.copy(isLoading = true)

        observeJob = viewModelScope.launch {

            repository.observeRooms().collect { result ->

                result
                    .onSuccess { rooms ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                rooms = rooms,
                                selectedRoom = _state.value.selectedRoom
                                    ?.let { selected ->
                                        rooms.find { it.id == selected.id }
                                            ?: selected
                                    }
                            )
                    }
                    .onFailure { exception ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = exception.message
                                    ?: "Failed to load rooms."
                            )
                    }
            }
        }
    }

    /** Kept for screens that call it on entry; the stream is already live. */
    fun loadRooms() {
        observeRooms()
    }

    fun loadRoomsForFloor(
        floorId: String
    ) {
        observeRooms()
    }

    fun loadRoom(
        roomId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null,
                    selectedRoom = null
                )

            repository.getRoom(roomId)
                .onSuccess { room ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            selectedRoom = room
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load room."
                        )
                }
        }
    }

    fun createRoom(
        name: String,
        floorId: String
    ) {

        if (name.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a room name."
                )
            return
        }

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.createRoom(
                Room(
                    name = name,
                    floorId = floorId
                )
            )
                .onSuccess { created ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            rooms = _state.value.rooms + created,
                            isSaved = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to create room."
                        )
                }
        }
    }

    fun updateRoom(
        roomId: String,
        name: String
    ) {

        if (name.isBlank()) {
            _state.value =
                _state.value.copy(
                    error = "Please enter a room name."
                )
            return
        }

        val existing =
            _state.value.rooms.find { it.id == roomId }
                ?: _state.value.selectedRoom?.takeIf { it.id == roomId }
                ?: return

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.updateRoom(
                existing.copy(name = name)
            )
                .onSuccess { updated ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            rooms = _state.value.rooms.map {
                                if (it.id == updated.id) updated else it
                            },
                            selectedRoom = updated,
                            isSaved = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to update room."
                        )
                }
        }
    }

    fun deleteRoom(
        roomId: String
    ) {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    isLoading = true,
                    error = null
                )

            repository.deleteRoom(roomId)
                .onSuccess {

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            rooms = _state.value.rooms.filterNot {
                                it.id == roomId
                            },
                            selectedRoom = null,
                            isDeleted = true
                        )
                }
                .onFailure { exception ->

                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to delete room."
                        )
                }
        }
    }

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
