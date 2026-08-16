package com.keeththigan.elyra.data.model

/**
 * The single operational status shown to the user.
 *
 * This is *derived* from power state + connectivity rather than stored:
 * a device that is switched on but unreachable is DISCONNECTED, not ON.
 * Connectivity always takes precedence over power because it describes
 * whether we can trust the power reading at all.
 */
enum class DeviceStatus {
    ON,
    OFF,
    ERROR,
    DISCONNECTED
}
