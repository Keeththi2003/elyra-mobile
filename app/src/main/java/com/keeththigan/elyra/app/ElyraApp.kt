package com.keeththigan.elyra.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keeththigan.elyra.app.navigation.AppNavigation
import com.keeththigan.elyra.core.connectivity.NetworkMonitor
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.preferences.ThemePreferences
import com.keeththigan.elyra.data.repository.AuthRepository
import com.keeththigan.elyra.data.repository.DeviceRepository
import com.keeththigan.elyra.data.repository.FloorRepository
import com.keeththigan.elyra.data.repository.RoomRepository
import com.keeththigan.elyra.feature.auth.AuthViewModel
import com.keeththigan.elyra.feature.auth.AuthViewModelFactory
import com.keeththigan.elyra.feature.auth.navigation.AuthNavigation
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.devices.DeviceViewModelFactory
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.FloorViewModelFactory
import com.keeththigan.elyra.feature.floors.RoomViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModelFactory
import com.keeththigan.elyra.feature.settings.appearance.AppearanceOption
import com.keeththigan.elyra.feature.settings.appearance.ThemeViewModel
import com.keeththigan.elyra.feature.settings.appearance.ThemeViewModelFactory

@Composable
fun ElyraApp() {

    val context = LocalContext.current

    val themePreferences = remember {
        ThemePreferences(context)
    }

    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(themePreferences)
    )

    val appearance by themeViewModel.appearance.collectAsStateWithLifecycle()

    val darkTheme = when (appearance) {
        AppearanceOption.LIGHT -> false
        AppearanceOption.DARK -> true
        AppearanceOption.SYSTEM -> isSystemInDarkTheme()
    }

    ElyraTheme(darkTheme = darkTheme) {

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

            val deviceRepository = remember {
                DeviceRepository()
            }

            val floorRepository = remember {
                FloorRepository()
            }

            val roomRepository = remember {
                RoomRepository()
            }

            /*
             * Keyed by the Firebase uid so signing out and back in as a
             * different user never reuses the previous account's cached data.
             *
             * The uid is read straight from FirebaseAuth, which has it
             * synchronously the moment we're authenticated. authState.user is
             * populated later by an async Firestore profile fetch, so keying on
             * that would flip the key null -> uid mid-session and silently swap
             * in fresh, empty ViewModels, orphaning the ones screens already hold.
             */
            val uid =
                authViewModel.getCurrentFirebaseUser()?.uid ?: "pending"

            val networkMonitor = remember {
                NetworkMonitor(context)
            }

            val deviceViewModel: DeviceViewModel = viewModel(
                key = "device_$uid",
                factory = DeviceViewModelFactory(
                    repository = deviceRepository,
                    networkMonitor = networkMonitor
                )
            )

            val floorViewModel: FloorViewModel = viewModel(
                key = "floor_$uid",
                factory = FloorViewModelFactory(
                    floorRepository = floorRepository,
                    roomRepository = roomRepository,
                    deviceRepository = deviceRepository
                )
            )

            val roomViewModel: RoomViewModel = viewModel(
                key = "room_$uid",
                factory = RoomViewModelFactory(
                    repository = roomRepository
                )
            )

            AppNavigation(
                authViewModel = authViewModel,
                deviceViewModel = deviceViewModel,
                floorViewModel = floorViewModel,
                roomViewModel = roomViewModel,
                themeViewModel = themeViewModel
            )

        } else {

            AuthNavigation(
                authViewModel = authViewModel
            )
        }
    }
}
