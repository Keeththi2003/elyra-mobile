package com.keeththigan.elyra.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.keeththigan.elyra.data.model.AppNotification
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotificationRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    private val notificationsCollection =
        firestore.collection("notifications")


    // ========================================================================
    // OBSERVE (realtime)
    // ========================================================================

    fun observeNotifications(): Flow<Result<List<AppNotification>>> =
        callbackFlow {

            val uid = auth.currentUser?.uid

            if (uid == null) {
                trySend(
                    Result.failure(Exception("You must be signed in."))
                )
                close()
                return@callbackFlow
            }

            val registration =
                notificationsCollection
                    .whereEqualTo("userId", uid)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(100)
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }

                        val notifications =
                            snapshot?.documents?.mapNotNull {
                                it.toObject(AppNotification::class.java)
                            } ?: emptyList()

                        trySend(Result.success(notifications))
                    }

            awaitClose { registration.remove() }
        }


    // ========================================================================
    // CREATE
    // ========================================================================

    suspend fun createNotification(
        notification: AppNotification
    ): Result<AppNotification> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val docRef = notificationsCollection.document()

            val stored =
                notification.copy(
                    id = docRef.id,
                    userId = uid,
                    createdAt = null
                )

            docRef.set(stored).await()

            Result.success(stored)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ========================================================================
    // MARK READ
    // ========================================================================

    suspend fun markAsRead(
        notificationId: String
    ): Result<Unit> = try {

        notificationsCollection
            .document(notificationId)
            .update("isRead", true)
            .await()

        Result.success(Unit)

    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun markAllAsRead(): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                notificationsCollection
                    .whereEqualTo("userId", uid)
                    .whereEqualTo("isRead", false)
                    .get()
                    .await()

            if (!snapshot.isEmpty) {

                val batch = firestore.batch()

                snapshot.documents.forEach { doc ->
                    batch.update(doc.reference, "isRead", true)
                }

                batch.commit().await()
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // ========================================================================
    // DELETE
    // ========================================================================

    suspend fun clearAll(): Result<Unit> {

        val uid =
            auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("You must be signed in.")
                )

        return try {

            val snapshot =
                notificationsCollection
                    .whereEqualTo("userId", uid)
                    .get()
                    .await()

            if (!snapshot.isEmpty) {

                val batch = firestore.batch()

                snapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }

                batch.commit().await()
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
