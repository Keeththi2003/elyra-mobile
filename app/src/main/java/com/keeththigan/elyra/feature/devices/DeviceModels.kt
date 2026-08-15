package com.keeththigan.elyra.feature.devices

enum class DeviceType {
    LIGHT,
    OUTLET,
    MULTI_SWITCH,
    SAFETY_APPLIANCE,
    SECURITY_CAMERA
}

enum class DeviceStatus {
    ON,
    OFF,
    ERROR,
    DISCONNECTED
}

data class Device(
    val id: String,
    val name: String,
    val type: DeviceType,
    val status: DeviceStatus,
    val roomId: String? = null,
    val floorId: String? = null,

    // Light
    val brightness: Int? = null,

    // Multi-switch
    val switchCount: Int? = null,

    // Safety appliance
    val maxOnDurationMinutes: Int? = null,

    // Security camera
    val cameraUri: String? = null
)