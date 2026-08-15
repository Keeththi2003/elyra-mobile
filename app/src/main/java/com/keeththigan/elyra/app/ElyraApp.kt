package com.keeththigan.elyra.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keeththigan.elyra.app.navigation.AppNavigation
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.repository.AuthRepository
import com.keeththigan.elyra.feature.auth.AuthViewModel
import com.keeththigan.elyra.feature.auth.AuthViewModelFactory
import com.keeththigan.elyra.feature.auth.navigation.AuthNavigation

@Composable
fun ElyraApp() {

    ElyraTheme {

        val authRepository = remember {
            AuthRepository()
        }

        val authViewModel: AuthViewModel = viewModel(
            factory = AuthViewModelFactory(
                repository = authRepository
            )
        )

        val authState =
            authViewModel.authState.collectAsStateWithLifecycle()

        LaunchedEffect(authState.value.isAuthenticated) {
            if (authState.value.isAuthenticated && authState.value.user == null) {
                authViewModel.loadUserProfile()
            }
        }

        if (authState.value.isAuthenticated) {

            AppNavigation(authViewModel = authViewModel)

        } else {

            AuthNavigation(
                authViewModel = authViewModel
            )
        }
    }
}