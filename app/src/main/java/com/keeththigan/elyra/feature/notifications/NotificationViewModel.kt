package com.keeththigan.elyra.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.keeththigan.elyra.core.notification.ElyraNotifier
import com.keeththigan.elyra.data.model.AppNotification
import com.keeththigan.elyra.data.preferences.ThemePreferences
import com.keeththigan.elyra.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<AppNotification> = emptyList(),
    val error: String? = null,
    val notificationsEnabled: Boolean = true
) {
    val unreadCount: Int
        get() = notifications.count { !it.isRead }
}


class NotificationViewModel(
    private val repository: NotificationRepository,
    private val notifier: ElyraNotifier,
    private val preferences: ThemePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(
        NotificationUiState(
            notificationsEnabled = preferences.areNotificationsEnabled()
        )
    )

    val state: StateFlow<NotificationUiState> =
        _state.asStateFlow()

    /** Ids already surfaced as a system notification this session. */
    private val alerted = mutableSetOf<String>()

    init {
        observeNotifications()
    }

    private fun observeNotifications() {

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {

            repository.observeNotifications().collect { result ->

                result
                    .onSuccess { notifications ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                notifications = notifications
                            )

                        surfaceNewAlerts(notifications)
                    }
                    .onFailure { exception ->

                        _state.value =
                            _state.value.copy(
                                isLoading = false,
                                error = exception.message
                                    ?: "Failed to load notifications."
                            )
                    }
            }
        }
    }

    /**
     * Raises a system notification for anything unread we have not already
     * shown. Driven off the Firestore stream, so an alert written by any
     * client surfaces here too.
     */
    private fun surfaceNewAlerts(
        notifications: List<AppNotification>
    ) {

        if (!_state.value.notificationsEnabled) return

        notifications
            .filter { !it.isRead && it.id !in alerted }
            .forEach { notification ->

                alerted += notification.id

                notifier.notifySafetyAlert(
                    id = notification.id.hashCode(),
                    title = notification.title,
                    message = notification.message
                )
            }
    }

    fun setNotificationsEnabled(
        enabled: Boolean
    ) {
        preferences.setNotificationsEnabled(enabled)
        _state.value = _state.value.copy(notificationsEnabled = enabled)
    }

    fun markAsRead(
        notificationId: String
    ) {
        viewModelScope.launch {
            repository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            repository.markAllAsRead()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}


class NotificationViewModelFactory(
    private val repository: NotificationRepository,
    private val notifier: ElyraNotifier,
    private val preferences: ThemePreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(
                repository,
                notifier,
                preferences
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
