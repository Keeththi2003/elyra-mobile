package com.keeththigan.elyra.feature.floors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keeththigan.elyra.data.repository.DeviceRepository
import com.keeththigan.elyra.data.repository.FloorRepository
import com.keeththigan.elyra.data.repository.RoomRepository

class FloorViewModelFactory(
    private val floorRepository: FloorRepository,
    private val roomRepository: RoomRepository,
    private val deviceRepository: DeviceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FloorViewModel::class.java)) {
            return FloorViewModel(
                floorRepository,
                roomRepository,
                deviceRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
