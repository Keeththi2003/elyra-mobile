package com.keeththigan.elyra.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.keeththigan.elyra.data.model.Room
import kotlinx.coroutines.tasks.await

class RoomRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    private val roomsCollection =
        firestore.collection("rooms")

    private val devicesCollection =
        firestore.collection("devices")


    // ========================================================================
    // CREATE
    // ========================================================================

    suspend fun createRoom(
        room: Room
    ): Result<Room> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val docRef =
                roomsCollection.document()

            val newRoom =
                room.copy(
                    id = docRef.id,
                    userId = uid,
                    createdAt = null,
                    updatedAt = null
                )

            docRef.set(newRoom).await()

            Result.success(newRoom)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // READ
    // ========================================================================

    suspend fun getRoom(
        roomId: String
    ): Result<Room> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                roomsCollection
                    .document(roomId)
                    .get()
                    .await()

            val room =
                snapshot.toObject(Room::class.java)
                    ?: return Result.failure(
                        Exception("Room not found.")
                    )

            if (room.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this room.")
                )
            }

            Result.success(room)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getRooms(): Result<List<Room>> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                roomsCollection
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

            val rooms =
                snapshot.documents.mapNotNull {
                    it.toObject(Room::class.java)
                }

            Result.success(rooms)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getRoomsForFloor(
        floorId: String
    ): Result<List<Room>> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                roomsCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("floorId", floorId)
                    .get()
                    .await()

            val rooms =
                snapshot.documents.mapNotNull {
                    it.toObject(Room::class.java)
                }

            Result.success(rooms)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // UPDATE
    // ========================================================================

    suspend fun updateRoom(
        room: Room
    ): Result<Room> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                roomsCollection
                    .document(room.id)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Room::class.java)
                    ?: return Result.failure(
                        Exception("Room not found.")
                    )

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this room.")
                )
            }

            val updatedRoom =
                room.copy(
                    userId = uid,
                    createdAt = existing.createdAt,
                    updatedAt = null
                )

            roomsCollection
                .document(room.id)
                .set(updatedRoom)
                .await()

            Result.success(updatedRoom)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    suspend fun deleteRoom(
        roomId: String
    ): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                roomsCollection
                    .document(roomId)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Room::class.java)
                    ?: return Result.success(Unit)

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this room.")
                )
            }

            // Cascade: unassign (don't delete) any devices in this room.
            val deviceSnapshot =
                devicesCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("roomId", roomId)
                    .get()
                    .await()

            if (!deviceSnapshot.isEmpty) {

                val batch = firestore.batch()

                deviceSnapshot.documents.forEach { doc ->
                    batch.update(doc.reference, "roomId", "")
                }

                batch.commit().await()
            }

            roomsCollection
                .document(roomId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
