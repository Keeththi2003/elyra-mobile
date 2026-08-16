package com.keeththigan.elyra.data.model

data class MultiSwitchConfig(
    val switchCount: Int = 2,
    val switches: List<SwitchChannel> = emptyList()
)