package com.keeththigan.elyra.feature.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keeththigan.elyra.core.connectivity.NetworkMonitor
import com.keeththigan.elyra.data.repository.DeviceRepository
import com.keeththigan.elyra.data.repository.NotificationRepository

class DeviceViewModelFactory(
    private val repository: DeviceRepository,
    private val networkMonitor: NetworkMonitor,
    private val notificationRepository: NotificationRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            return DeviceViewModel(
                repository,
                networkMonitor,
                notificationRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
