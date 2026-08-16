package com.keeththigan.elyra.data.repository

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<User> {

        return try {

            val authResult =
                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            val firebaseUser =
                authResult.user
                    ?: return Result.failure(
                        Exception("Failed to create user account.")
                    )

            val user = User(
                id = firebaseUser.uid,
                name = name.trim(),
                email = email.trim()
            )

            usersCollection
                .document(firebaseUser.uid)
                .set(user)
                .await()

            Result.success(user)

        } catch (e: Exception) {

            // Roll back the auth account so the user isn't left in a
            // half-created state (auth account exists, no Firestore profile).
            // Best-effort: don't let a rollback failure mask the original error.
            try {
                auth.currentUser?.delete()?.await()
            } catch (_: Exception) {
            }

            Result.failure(e)
        }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {

        return try {

            val authResult =
                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            val firebaseUser =
                authResult.user
                    ?: return Result.failure(
                        Exception("Failed to sign in.")
                    )

            val user = try {
                usersCollection
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                    .toObject(User::class.java)
            } catch (e: Exception) {
                null
            }

            if (user != null) {
                return Result.success(user)
            }

            val repairedUser = User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName
                    ?.takeIf { it.isNotBlank() }
                    ?: email.substringBefore("@"),
                email = firebaseUser.email ?: email.trim()
            )

            try {
                usersCollection
                    .document(firebaseUser.uid)
                    .set(repairedUser)
                    .await()
            } catch (_: Exception) {
            }

            Result.success(repairedUser)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Result<Unit> {

        return try {

            auth.sendPasswordResetEmail(
                email.trim()
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun getUserProfile(
        uid: String
    ): Result<User> {

        return try {

            val snapshot =
                usersCollection
                    .document(uid)
                    .get()
                    .await()

            val user =
                snapshot.toObject(User::class.java)
                    ?: return Result.failure(
                        Exception("User profile not found.")
                    )

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}
