package com.keeththigan.elyra.data.model

/**
 * One individually addressable switch inside a multi-switch gang box.
 */
data class SwitchChannel(
    val index: Int = 0,
    val name: String = "",
    val isOn: Boolean = false
)
