package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

data class Device(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val type: DeviceType = DeviceType.LIGHT,

    val roomId: String = "",
    val floorId: String = "",

    /**
     * Power state, the one field the user controls directly.
     *
     * @PropertyName is required: Firestore's mapper strips the `is` prefix
     * from the generated `isOn()` getter, so it would write `on` but read
     * back `isOn` and silently deserialise every device to off.
     */
    @get:PropertyName("isOn")
    @set:PropertyName("isOn")
    var isOn: Boolean = false,

    /** Link health — reported by the device, never set by the user. */
    val connectivity: DeviceConnectivity = DeviceConnectivity.ONLINE,

    // Light only.
    val brightness: Int? = null,

    /** Multi-switch only — each channel is named and addressed independently. */
    val switches: List<SwitchChannel> = emptyList(),

    /** Safety appliance only — how long it may run before the cutoff fires. */
    val maxOnDurationMinutes: Int? = null,

    // Security camera only.
    val cameraUri: String? = null,

    val scheduleEnabled: Boolean = false,
    val scheduleStart: String? = null,
    val scheduleEnd: String? = null,

    /**
     * When the device was last switched ON. Drives the safety cutoff and
     * accumulated usage. Set explicitly, so it is not a server timestamp.
     */
    val lastOnAt: Timestamp? = null,

    /** Accumulated ON time in seconds, for usage reporting. */
    val totalOnSeconds: Long = 0,

    /** Set when the safety cutoff forced this device off. */
    val lastSafetyCutoffAt: Timestamp? = null,

    @ServerTimestamp
    val createdAt: Timestamp? = null,
    @ServerTimestamp
    val updatedAt: Timestamp? = null
) {

    /**
     * The status surfaced in the UI. Connectivity wins over power: an unreachable
     * device is DISCONNECTED even if we last knew it to be on.
     *
     * @Exclude keeps this out of the Firestore document — it is always derived.
     */
    @get:Exclude
    val status: DeviceStatus
        get() = when {
            connectivity == DeviceConnectivity.ERROR -> DeviceStatus.ERROR
            connectivity == DeviceConnectivity.OFFLINE -> DeviceStatus.DISCONNECTED
            isOn -> DeviceStatus.ON
            else -> DeviceStatus.OFF
        }

    /** True when the user is allowed to operate the device right now. */
    @get:Exclude
    val isControllable: Boolean
        get() = connectivity == DeviceConnectivity.ONLINE

    /** Channels currently switched on, for multi-switch units. */
    @get:Exclude
    val activeChannelCount: Int
        get() = switches.count { it.isOn }
}
