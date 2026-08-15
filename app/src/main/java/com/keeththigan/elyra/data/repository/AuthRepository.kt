package com.keeththigan.elyra.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.keeththigan.elyra.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

    private val usersCollection =
        firestore.collection("users")


    // ============================================================
    // REGISTER
    // ============================================================

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<User> {

        return try {

            // Firebase Authentication creates the account
            val result =
                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            val firebaseUser =
                result.user
                    ?: return Result.failure(
                        Exception("User registration failed")
                    )

            val now = Timestamp.now()

            val user = User(
                id = firebaseUser.uid,
                name = name.trim(),
                email = email.trim(),
                createdAt = now,
                updatedAt = now
            )

            // Store profile information in Firestore
            usersCollection
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ============================================================
    // LOGIN
    // ============================================================

    suspend fun login(
        email: String,
        password: String
    ): Result<User> {

        return try {

            val result =
                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            val firebaseUser =
                result.user
                    ?: return Result.failure(
                        Exception("Login failed")
                    )

            val user =
                getUser(firebaseUser.uid)

            if (user != null) {

                Result.success(user)

            } else {

                Result.failure(
                    Exception("User profile not found")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ============================================================
    // GET CURRENT USER
    // ============================================================

    suspend fun getCurrentUser(): User? {

        val firebaseUser =
            auth.currentUser
                ?: return null

        return getUser(firebaseUser.uid)
    }


    // ============================================================
    // GET USER BY ID
    // ============================================================

    suspend fun getUser(
        userId: String
    ): User? {

        return try {

            val snapshot =
                usersCollection
                    .document(userId)
                    .get()
                    .await()

            snapshot.toObject(User::class.java)

        } catch (e: Exception) {

            null
        }
    }


    // ============================================================
    // CURRENT FIREBASE USER
    // ============================================================

    fun getFirebaseUser(): FirebaseUser? {

        return auth.currentUser
    }


    // ============================================================
    // CHECK LOGIN STATE
    // ============================================================

    fun isLoggedIn(): Boolean {

        return auth.currentUser != null
    }


    // ============================================================
    // LOGOUT
    // ============================================================

    fun logout() {

        auth.signOut()
    }


    // ============================================================
    // UPDATE USER PROFILE
    // ============================================================

    suspend fun updateProfile(
        name: String
    ): Result<Unit> {

        return try {

            val firebaseUser =
                auth.currentUser
                    ?: return Result.failure(
                        Exception("User is not logged in")
                    )

            val updates = mapOf(
                "name" to name.trim(),
                "updatedAt" to Timestamp.now()
            )

            usersCollection
                .document(firebaseUser.uid)
                .update(updates)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ============================================================
    // DELETE ACCOUNT
    // ============================================================

    suspend fun deleteAccount(): Result<Unit> {

        return try {

            val firebaseUser =
                auth.currentUser
                    ?: return Result.failure(
                        Exception("User is not logged in")
                    )

            val userId =
                firebaseUser.uid

            // Delete Firestore profile
            usersCollection
                .document(userId)
                .delete()
                .await()

            // Delete Firebase Authentication account
            firebaseUser
                .delete()
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}