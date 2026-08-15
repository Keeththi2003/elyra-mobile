package com.keeththigan.elyra.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keeththigan.elyra.data.model.User
import com.keeththigan.elyra.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ============================================================================
// AUTH STATE
// ============================================================================

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val error: String? = null
)


// ============================================================================
// AUTH VIEW MODEL
// ============================================================================

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


    // ========================================================================
    // SIGN UP
    // ========================================================================

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
                isLoading = true
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


    // ========================================================================
    // SIGN IN
    // ========================================================================

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
                isLoading = true
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


    // ========================================================================
    // SIGN OUT
    // ========================================================================

    fun signOut() {

        repository.signOut()

        _authState.value = AuthState(
            isAuthenticated = false
        )
    }


    // ========================================================================
    // CLEAR ERROR
    // ========================================================================

    fun clearError() {

        _authState.value =
            _authState.value.copy(
                error = null
            )
    }


    // ========================================================================
    // CURRENT USER
    // ========================================================================

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }


    // ========================================================================
    // LOAD USER PROFILE
    // ========================================================================

    fun loadUserProfile() {

        val firebaseUser =
            repository.getCurrentUser()
                ?: return

        viewModelScope.launch {

            _authState.value =
                _authState.value.copy(
                    isLoading = true
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


    // ========================================================================
    // FIREBASE ERROR MESSAGES
    // ========================================================================

    private fun getFirebaseErrorMessage(
        exception: Throwable
    ): String {

        val message =
            exception.message ?: return "Something went wrong."

        return when {

            message.contains(
                "already in use",
                ignoreCase = true
            ) ->
                "This email is already registered."

            message.contains(
                "badly formatted",
                ignoreCase = true
            ) ->
                "Please enter a valid email address."

            message.contains(
                "password is invalid",
                ignoreCase = true
            ) ->
                "Incorrect password."

            message.contains(
                "no user record",
                ignoreCase = true
            ) ->
                "No account found with this email."

            message.contains(
                "network",
                ignoreCase = true
            ) ->
                "Network error. Please check your connection."

            message.contains(
                "profile not found",
                ignoreCase = true
            ) ->
                "User profile could not be found."

            else ->
                message
        }
    }
}