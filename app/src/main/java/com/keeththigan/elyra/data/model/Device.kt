package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Device(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val type: DeviceType = DeviceType.LIGHT,
    val status: DeviceStatus = DeviceStatus.OFF,
    val roomId: String = "",
    val floorId: String = "",

    // Light
    val brightness: Int? = null,

    // Multi-switch
    val switchCount: Int? = null,

    // Safety appliance
    val maxOnDurationMinutes: Int? = null,

    // Security camera
    val cameraUri: String? = null,

    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
)
