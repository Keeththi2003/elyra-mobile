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


    // ========================================================================
    // CURRENT USER
    // ========================================================================

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }


    // ========================================================================
    // SIGN UP
    // ========================================================================

    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<User> {

        return try {

            // ------------------------------------------------------------
            // Create Firebase Authentication account
            // ------------------------------------------------------------

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

            // ------------------------------------------------------------
            // Create application user
            // ------------------------------------------------------------

            val user = User(
                id = firebaseUser.uid,
                name = name.trim(),
                email = email.trim()
            )

            // ------------------------------------------------------------
            // Save user profile in Firestore
            // ------------------------------------------------------------

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


    // ========================================================================
    // SIGN IN
    // ========================================================================

    suspend fun signIn(
        email: String,
        password: String
    ): Result<User> {

        return try {

            // ------------------------------------------------------------
            // Firebase Authentication
            // ------------------------------------------------------------

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

            // ------------------------------------------------------------
            // Get application user from Firestore
            // ------------------------------------------------------------

            val snapshot =
                usersCollection
                    .document(firebaseUser.uid)
                    .get()
                    .await()

            val user =
                snapshot.toObject(User::class.java)

            if (user == null) {

                val repairedUser = User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName
                        ?.takeIf { it.isNotBlank() }
                        ?: email.substringBefore("@"),
                    email = firebaseUser.email ?: email.trim()
                )

                usersCollection
                    .document(firebaseUser.uid)
                    .set(repairedUser)
                    .await()

                return Result.success(repairedUser)
            }

            Result.success(user)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


    // ========================================================================
    // PASSWORD RESET
    // ========================================================================

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


    // ========================================================================
    // SIGN OUT
    // ========================================================================

    fun signOut() {
        auth.signOut()
    }


    // ========================================================================
    // GET USER PROFILE
    // ========================================================================

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