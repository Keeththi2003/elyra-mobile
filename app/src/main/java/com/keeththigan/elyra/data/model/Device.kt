package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp

data class Device(
    val id: String = "",
    val name: String = "",

    val type: DeviceType = DeviceType.LIGHT,
    val status: DeviceStatus = DeviceStatus.OFF,

    val isOn: Boolean = false,

    val floorId: String = "",
    val roomId: String = "",

    val lightConfig: LightConfig? = null,

    val multiSwitchConfig: MultiSwitchConfig? = null,

    val ironConfig: IronConfig? = null,

    val cameraConfig: CameraConfig? = null,

    val acConfig: AcConfig? = null,

    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)