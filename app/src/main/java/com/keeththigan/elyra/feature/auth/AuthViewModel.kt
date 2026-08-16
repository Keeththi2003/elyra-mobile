package com.keeththigan.elyra.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.keeththigan.elyra.data.model.User
import com.keeththigan.elyra.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val message: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(
        AuthState(
            isAuthenticated = repository.getCurrentUser() != null
        )
    )

    val authState: StateFlow<AuthState> =
        _authState.asStateFlow()

    fun signUp(
        name: String,
        email: String,
        password: String
    ) {

        if (name.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter your name."
            )
            return
        }

        if (email.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter your email."
            )
            return
        }

        if (password.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter a password."
            )
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState(
                error = "Password must contain at least 6 characters."
            )
            return
        }

        viewModelScope.launch {

            _authState.value = AuthState(
                isLoading = true,
                message = null
            )

            val result = repository.signUp(
                name = name,
                email = email,
                password = password
            )

            result
                .onSuccess { user ->

                    _authState.value = AuthState(
                        isLoading = false,
                        isAuthenticated = true,
                        error = null,
                        message = null,
                        user = user
                    )
                }
                .onFailure { exception ->

                    _authState.value = AuthState(
                        isLoading = false,
                        error = getFirebaseErrorMessage(
                            exception
                        )
                    )
                }
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {

        if (email.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter your email."
            )
            return
        }

        if (password.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter your password."
            )
            return
        }

        viewModelScope.launch {

            _authState.value = AuthState(
                isLoading = true,
                message = null
            )

            val result = repository.signIn(
                email = email,
                password = password
            )

            result
                .onSuccess { user ->

                    _authState.value = AuthState(
                        isLoading = false,
                        isAuthenticated = true,
                        error = null,
                        message = null,
                        user = user
                    )
                }
                .onFailure { exception ->

                    _authState.value = AuthState(
                        isLoading = false,
                        error = getFirebaseErrorMessage(
                            exception
                        )
                    )
                }
        }
    }

    fun sendPasswordResetEmail(
        email: String
    ) {

        if (email.isBlank()) {
            _authState.value = AuthState(
                error = "Please enter your email."
            )
            return
        }

        viewModelScope.launch {

            _authState.value = AuthState(
                isLoading = true,
                message = null
            )

            val result = repository.sendPasswordResetEmail(
                email = email
            )

            result
                .onSuccess {

                    _authState.value = AuthState(
                        isLoading = false,
                        message = "Password reset email sent. Check your inbox."
                    )
                }
                .onFailure { exception ->

                    _authState.value = AuthState(
                        isLoading = false,
                        error = getFirebaseErrorMessage(
                            exception
                        )
                    )
                }
        }
    }

    fun signOut() {

        repository.signOut()

        _authState.value = AuthState(
            isAuthenticated = false,
            message = null
        )
    }

    fun clearError() {

        _authState.value =
            _authState.value.copy(
                error = null
            )
    }

    fun clearMessage() {

        _authState.value = _authState.value.copy(
            message = null
        )
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    fun loadUserProfile() {

        val firebaseUser =
            repository.getCurrentUser()
                ?: return

        viewModelScope.launch {

            _authState.value =
                _authState.value.copy(
                            isLoading = true,
                            error = null,
                            message = null
                )

            val result =
                repository.getUserProfile(
                    firebaseUser.uid
                )

            result
                .onSuccess { user ->

                    _authState.value =
                        AuthState(
                            isLoading = false,
                            isAuthenticated = true,
                            user = user
                        )
                }
                .onFailure { exception ->

                    _authState.value =
                        AuthState(
                            isLoading = false,
                            isAuthenticated = true,
                            error = exception.message
                                ?: "Failed to load user profile."
                        )
                }
        }
    }

    private fun getFirebaseErrorMessage(
        exception: Throwable
    ): String {

        val message = exception.message ?: ""

        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                return "This email is already registered."
            }

            is FirebaseAuthInvalidUserException -> {
                return "No account found with this email."
            }

            is FirebaseAuthInvalidCredentialsException -> {
                return "Please check your email and password."
            }

            is FirebaseAuthException -> {
                return when (exception.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already registered."
                    "ERROR_INVALID_EMAIL" -> "Please enter a valid email address."
                    "ERROR_WRONG_PASSWORD" -> "Incorrect password."
                    "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                    "ERROR_USER_DISABLED" -> "This account has been disabled."
                    "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later."
                    "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your connection."
                    "ERROR_WEAK_PASSWORD" -> "Password must contain at least 6 characters."
                    "ERROR_OPERATION_NOT_ALLOWED" -> "Email and password sign-in is disabled for this project."
                    "PERMISSION_DENIED" -> "Your account was created, but Firestore blocked the profile save. Deploy the Firestore rules in this project."
                    else -> exception.message ?: "Something went wrong."
                }
            }
        }

        return when {

            message.contains(
                "already in use",
                ignoreCase = true
            ) ->
                "This email is already registered."

            message.contains(
                "invalid email",
                ignoreCase = true
            ) ->
                "Please enter a valid email address."

            message.contains(
                "badly formatted",
                ignoreCase = true
            ) ->
                "Please enter a valid email address."

            message.contains(
                "wrong password",
                ignoreCase = true
            ) ->
                "Incorrect password."

            message.contains(
                "password is invalid",
                ignoreCase = true
            ) ->
                "Incorrect password."

            message.contains(
                "too many requests",
                ignoreCase = true
            ) ->
                "Too many attempts. Try again later."

            message.contains(
                "operation not allowed",
                ignoreCase = true
            ) ->
                "Email and password sign-in is disabled for this project."

            message.contains(
                "no user record",
                ignoreCase = true
            ) ->
                "No account found with this email."

            message.contains(
                "user disabled",
                ignoreCase = true
            ) ->
                "This account has been disabled."

            message.contains(
                "network request failed",
                ignoreCase = true
            ) ->
                "Network error. Please check your connection."

            message.contains(
                "network",
                ignoreCase = true
            ) ->
                "Network error. Please check your connection."

            message.contains(
                "weak password",
                ignoreCase = true
            ) ->
                "Password must contain at least 6 characters."

            message.contains(
                "profile not found",
                ignoreCase = true
            ) ->
                "User profile could not be found."

            message.contains(
                "permission_denied",
                ignoreCase = true
            ) || message.contains(
                "missing or insufficient permissions",
                ignoreCase = true
            ) ->
                "Your account was created, but Firestore blocked the profile save. Deploy the Firestore rules in this project."

            else ->
                if (message.isBlank()) {
                    "Something went wrong."
                } else {
                    message
                }
        }
    }
}
