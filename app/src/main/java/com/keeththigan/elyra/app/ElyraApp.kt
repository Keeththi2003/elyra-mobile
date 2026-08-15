package com.keeththigan.elyra.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keeththigan.elyra.app.navigation.AppNavigation
import com.keeththigan.elyra.core.designsystem.ElyraTheme
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

            val deviceRepository = remember {
                DeviceRepository()
            }

            val floorRepository = remember {
                FloorRepository()
            }

            val roomRepository = remember {
                RoomRepository()
            }

            // Keyed by uid so signing out and back in as a different user
            // never reuses another account's cached devices/floors/rooms.
           val uid = authState.value.user?.id ?: return@ElyraTheme

val deviceViewModel: DeviceViewModel = viewModel(
    key = "device_$uid",
    factory = DeviceViewModelFactory(
        repository = deviceRepository
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
                roomViewModel = roomViewModel
            )

        } else {

            AuthNavigation(
                authViewModel = authViewModel
            )
        }
    }
}