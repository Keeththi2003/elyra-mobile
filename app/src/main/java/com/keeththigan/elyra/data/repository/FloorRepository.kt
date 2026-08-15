package com.keeththigan.elyra.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.keeththigan.elyra.data.model.Floor
import kotlinx.coroutines.tasks.await

class FloorRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    private val floorsCollection =
        firestore.collection("floors")

    private val roomsCollection =
        firestore.collection("rooms")

    private val devicesCollection =
        firestore.collection("devices")


    // ========================================================================
    // CREATE
    // ========================================================================

    suspend fun createFloor(
        floor: Floor
    ): Result<Floor> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val docRef =
                floorsCollection.document()

            val newFloor =
                floor.copy(
                    id = docRef.id,
                    userId = uid,
                    createdAt = null,
                    updatedAt = null
                )

            docRef.set(newFloor).await()

            Result.success(newFloor)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // READ
    // ========================================================================

    suspend fun getFloor(
        floorId: String
    ): Result<Floor> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                floorsCollection
                    .document(floorId)
                    .get()
                    .await()

            val floor =
                snapshot.toObject(Floor::class.java)
                    ?: return Result.failure(
                        Exception("Floor not found.")
                    )

            if (floor.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this floor.")
                )
            }

            Result.success(floor)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getFloors(): Result<List<Floor>> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                floorsCollection
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

            val floors =
                snapshot.documents.mapNotNull {
                    it.toObject(Floor::class.java)
                }

            Result.success(floors)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // UPDATE
    // ========================================================================

    suspend fun updateFloor(
        floor: Floor
    ): Result<Floor> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                floorsCollection
                    .document(floor.id)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Floor::class.java)
                    ?: return Result.failure(
                        Exception("Floor not found.")
                    )

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this floor.")
                )
            }

            val updatedFloor =
                floor.copy(
                    userId = uid,
                    createdAt = existing.createdAt,
                    updatedAt = null
                )

            floorsCollection
                .document(floor.id)
                .set(updatedFloor)
                .await()

            Result.success(updatedFloor)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    suspend fun deleteFloor(
        floorId: String
    ): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                floorsCollection
                    .document(floorId)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Floor::class.java)
                    ?: return Result.success(Unit)

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this floor.")
                )
            }

            // Cascade: unassign (don't delete) devices on this floor.
            val deviceSnapshot =
                devicesCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("floorId", floorId)
                    .get()
                    .await()

            if (!deviceSnapshot.isEmpty) {

                val deviceBatch = firestore.batch()

                deviceSnapshot.documents.forEach { doc ->
                    deviceBatch.update(
                        doc.reference,
                        mapOf(
                            "floorId" to "",
                            "roomId" to ""
                        )
                    )
                }

                deviceBatch.commit().await()
            }

            // Cascade: delete this floor's rooms (rooms have no meaning
            // without a floor).
            val roomSnapshot =
                roomsCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("floorId", floorId)
                    .get()
                    .await()

            if (!roomSnapshot.isEmpty) {

                val roomBatch = firestore.batch()

                roomSnapshot.documents.forEach { doc ->
                    roomBatch.delete(doc.reference)
                }

                roomBatch.commit().await()
            }

            floorsCollection
                .document(floorId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
