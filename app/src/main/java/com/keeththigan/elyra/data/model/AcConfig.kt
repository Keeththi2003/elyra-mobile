package com.keeththigan.elyra.data.model

data class AcConfig(
    val targetTemperature: Int = 24,
    val minTemperature: Int = 16,
    val maxTemperature: Int = 30
)