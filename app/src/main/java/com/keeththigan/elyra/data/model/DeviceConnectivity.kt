package com.keeththigan.elyra.data.model

/**
 * Link health between the hub and the physical device.
 *
 * This is reported by the device/gateway — never chosen by the user. The
 * user only controls power (see [Device.isOn]); whether the unit is
 * reachable is a fact about the hardware.
 */
enum class DeviceConnectivity {

    /** Reachable and reporting normally. */
    ONLINE,

    /** Not reachable — powered down at the wall, off the network, etc. */
    OFFLINE,

    /** Reachable but reporting a fault. */
    ERROR
}
