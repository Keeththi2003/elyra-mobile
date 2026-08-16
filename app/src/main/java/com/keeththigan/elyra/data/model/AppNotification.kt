package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

enum class NotificationType {
    /** A safety-critical appliance exceeded its maximum ON duration. */
    SAFETY_CUTOFF,

    /** A device started reporting a fault. */
    DEVICE_ERROR,

    /** A device dropped off the network. */
    DEVICE_OFFLINE
}

/**
 * An alert raised by the system and kept in Firestore so the history is the
 * same on every device the user signs in to.
 */
data class AppNotification(
    val id: String = "",
    val userId: String = "",
    val type: NotificationType = NotificationType.SAFETY_CUTOFF,
    val title: String = "",
    val message: String = "",
    val deviceId: String = "",
    val deviceName: String = "",

    @get:PropertyName("isRead")
    @set:PropertyName("isRead")
    var isRead: Boolean = false,

    @ServerTimestamp
    val createdAt: Timestamp? = null
)
