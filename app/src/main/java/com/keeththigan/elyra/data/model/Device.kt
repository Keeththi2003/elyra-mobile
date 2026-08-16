package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Device(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val type: DeviceType = DeviceType.LIGHT,

    val roomId: String = "",
    val floorId: String = "",

    /** Power state — this is what the user actually controls. */
    val isOn: Boolean = false,

    /** Link health — reported by the device, never set by the user. */
    val connectivity: DeviceConnectivity = DeviceConnectivity.ONLINE,

    // ---- Light ----------------------------------------------------------
    val brightness: Int? = null,

    // ---- Multi-switch ---------------------------------------------------
    /** Each channel is named and addressed independently. */
    val switches: List<SwitchChannel> = emptyList(),

    // ---- Safety appliance -----------------------------------------------
    /** Maximum permissible active duration before the safety cutoff fires. */
    val maxOnDurationMinutes: Int? = null,

    // ---- Security camera ------------------------------------------------
    val cameraUri: String? = null,

    // ---- Scheduling -----------------------------------------------------
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
