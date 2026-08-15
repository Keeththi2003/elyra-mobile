package com.keeththigan.elyra.data.model

data class LightConfig(
    val brightness: Int = 100,
    val scheduleEnabled: Boolean = false,
    val scheduleStart: String? = null,
    val scheduleEnd: String? = null
)