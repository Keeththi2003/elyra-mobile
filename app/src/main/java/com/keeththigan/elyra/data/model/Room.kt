package com.keeththigan.elyra.data.model

import com.google.firebase.Timestamp

data class Room(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val floorId: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)