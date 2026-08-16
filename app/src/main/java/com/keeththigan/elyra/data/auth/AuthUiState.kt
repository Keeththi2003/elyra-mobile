package com.keeththigan.elyra.data.auth

import com.keeththigan.elyra.data.model.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)
