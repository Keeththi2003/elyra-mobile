package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Device(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val type: DeviceType = DeviceType.LIGHT,
    val status: DeviceStatus = DeviceStatus.OFF,
    val roomId: String = "",
    val floorId: String = "",

    // Light
    val brightness: Int? = null,

    // Multi-switch: each channel is addressed independently.
    val switchCount: Int? = null,
    val switches: List<SwitchChannel> = emptyList(),

    // Safety appliance (e.g. iron): maximum permissible active duration.
    val maxOnDurationMinutes: Int? = null,

    // Security camera
    val cameraUri: String? = null,

    // Scheduling: lights and safety appliances may auto-run in a time window.
    val scheduleEnabled: Boolean = false,
    val scheduleStart: String? = null,
    val scheduleEnd: String? = null,

    /**
     * When the device was last switched ON. Drives both the safety cutoff
     * and accumulated usage reporting. Set explicitly (not a server timestamp)
     * so the elapsed-time maths stays simple.
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
)
