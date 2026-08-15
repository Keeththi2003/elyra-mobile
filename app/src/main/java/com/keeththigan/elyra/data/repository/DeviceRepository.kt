package com.keeththigan.elyra.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.keeththigan.elyra.data.model.Device
import kotlinx.coroutines.tasks.await

class DeviceRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    private val devicesCollection =
        firestore.collection("devices")


    // ========================================================================
    // CREATE
    // ========================================================================

    suspend fun createDevice(
        device: Device
    ): Result<Device> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val docRef =
                devicesCollection.document()

            val newDevice =
                device.copy(
                    id = docRef.id,
                    userId = uid,
                    createdAt = null,
                    updatedAt = null
                )

            docRef.set(newDevice).await()

            Result.success(newDevice)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // READ
    // ========================================================================

    suspend fun getDevice(
        deviceId: String
    ): Result<Device> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                devicesCollection
                    .document(deviceId)
                    .get()
                    .await()

            val device =
                snapshot.toObject(Device::class.java)
                    ?: return Result.failure(
                        Exception("Device not found.")
                    )

            if (device.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this device.")
                )
            }

            Result.success(device)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getDevices(): Result<List<Device>> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                devicesCollection
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

            val devices =
                snapshot.documents.mapNotNull {
                    it.toObject(Device::class.java)
                }

            Result.success(devices)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // UPDATE
    // ========================================================================

    suspend fun updateDevice(
        device: Device
    ): Result<Device> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                devicesCollection
                    .document(device.id)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Device::class.java)
                    ?: return Result.failure(
                        Exception("Device not found.")
                    )

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this device.")
                )
            }

            val updatedDevice =
                device.copy(
                    userId = uid,
                    createdAt = existing.createdAt,
                    updatedAt = null
                )

            devicesCollection
                .document(device.id)
                .set(updatedDevice)
                .await()

            Result.success(updatedDevice)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    suspend fun deleteDevice(
        deviceId: String
    ): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val existingSnapshot =
                devicesCollection
                    .document(deviceId)
                    .get()
                    .await()

            val existing =
                existingSnapshot.toObject(Device::class.java)
                    ?: return Result.success(Unit)

            if (existing.userId != uid) {
                return Result.failure(
                    Exception("You don't have access to this device.")
                )
            }

            devicesCollection
                .document(deviceId)
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // CASCADE HELPERS (used by FloorRepository / RoomRepository)
    // ========================================================================

    suspend fun unassignDevicesFromRoom(
        roomId: String
    ): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                devicesCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("roomId", roomId)
                    .get()
                    .await()

            if (!snapshot.isEmpty) {

                val batch = firestore.batch()

                snapshot.documents.forEach { doc ->
                    batch.update(doc.reference, "roomId", "")
                }

                batch.commit().await()
            }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun unassignDevicesFromFloor(
        floorId: String
    ): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                devicesCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("floorId", floorId)
                    .get()
                    .await()

            if (!snapshot.isEmpty) {

                val batch = firestore.batch()

                snapshot.documents.forEach { doc ->
                    batch.update(
                        doc.reference,
                        mapOf(
                            "floorId" to "",
                            "roomId" to ""
                        )
                    )
                }

                batch.commit().await()
            }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
