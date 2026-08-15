package com.keeththigan.elyra.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keeththigan.elyra.feature.devices.DeviceDetailScreen
import com.keeththigan.elyra.feature.devices.DevicesScreen
import com.keeththigan.elyra.feature.devices.AddDeviceScreen
import com.keeththigan.elyra.feature.home.presentation.HomeScreen
import com.keeththigan.elyra.feature.settings.SettingsScreen
import com.keeththigan.elyra.feature.settings.appearance.AppearanceScreen
import com.keeththigan.elyra.feature.settings.about.AboutScreen
import com.keeththigan.elyra.feature.floors.FloorsScreen
import com.keeththigan.elyra.feature.floors.FloorDetailScreen
import com.keeththigan.elyra.feature.floors.EditFloorScreen
import com.keeththigan.elyra.feature.floors.EditRoomScreen
import com.keeththigan.elyra.feature.devices.EditDeviceScreen
import com.keeththigan.elyra.feature.floors.RoomDetailsScreen
import com.keeththigan.elyra.feature.floors.AddRoomScreen
import com.keeththigan.elyra.feature.floors.AddFloorScreen
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.feature.auth.AuthViewModel
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel


private object AppRoutes {

    const val HOME = "home"
    const val DEVICES = "devices"
    const val FLOORS = "floors"
    const val SETTINGS = "settings"

const val DEVICE_DETAIL = "device_detail/{deviceId}"
const val FLOOR_DETAIL = "floor_detail/{floorId}"
const val ADD_FLOOR = "add_floor"
const val ADD_DEVICE = "add_device"
const val ADD_ROOM = "add_room/{floorId}"
const val ROOM_DETAILS = "room_details/{roomId}"
const val EDIT_FLOOR = "edit_floor/{floorId}"
const val EDIT_ROOM = "edit_room/{roomId}"
const val EDIT_DEVICE = "edit_device/{deviceId}"
    const val APPEARANCE = "appearance"
    const val ABOUT = "about"
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomNavItems = listOf(

    BottomNavItem(
        route = AppRoutes.HOME,
        label = "Home",
        icon = Icons.Outlined.Home
    ),

    BottomNavItem(
        route = AppRoutes.DEVICES,
        label = "Devices",
        icon = Icons.Outlined.Devices
    ),

    BottomNavItem(
        route = AppRoutes.FLOORS,
        label = "Floors",
        icon = Icons.Outlined.Layers
    ),

    BottomNavItem(
        route = AppRoutes.SETTINGS,
        label = "Settings",
        icon = Icons.Outlined.Settings
    )
)

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    deviceViewModel: DeviceViewModel,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel
) {

    val navController = rememberNavController()

    val navBackStackEntry by
        navController.currentBackStackEntryAsState()

    val currentDestination =
        navBackStackEntry?.destination

    val currentRoute =
        currentDestination?.route

    /*
     * Bottom navigation is only visible
     * on the three main application screens.
     *
     * Device Detail gets its own screen
     * without the bottom navigation.
     */
    val showBottomBar =
        currentRoute == AppRoutes.HOME ||
        currentRoute == AppRoutes.DEVICES ||
        currentRoute == AppRoutes.FLOORS ||
        currentRoute == AppRoutes.SETTINGS

    Scaffold(

        bottomBar = {

    if (showBottomBar) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    ElyraTheme.colors.surface
                )
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            bottomNavItems.forEach { item ->

                val selected =
                    currentDestination
                        ?.hierarchy
                        ?.any {
                            it.route == item.route
                        } == true

                ElyraBottomBarItem(
                    item = item,
                    selected = selected,
                    onClick = {

                        if (currentRoute != item.route) {

                            navController.navigate(item.route) {

                                popUpTo(AppRoutes.HOME) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            // =====================================================
            // HOME
            // =====================================================

            composable(AppRoutes.HOME) {

    HomeScreen(

        deviceViewModel = deviceViewModel,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,

        onFloorClick = { floorId ->

            navController.navigate(
                "floor_detail/$floorId"
            )
        },

        onDeviceClick = { deviceId ->

            navController.navigate(
                "device_detail/$deviceId"
            )
        },

        onAddFloor = {

            navController.navigate(
                AppRoutes.ADD_FLOOR
            )
        },

        onAddDevice = {

            navController.navigate(
                AppRoutes.ADD_DEVICE
            )
        },

        onProfileClick = {

            navController.navigate(
                AppRoutes.SETTINGS
            )
        }
    )
}

            // =====================================================
            // DEVICES
            // =====================================================

            composable(AppRoutes.DEVICES) {

    DevicesScreen(

        deviceViewModel = deviceViewModel,

        onDeviceClick = { deviceId ->

            navController.navigate(
                "device_detail/$deviceId"
            )
        },
        onAddDevice = {
    navController.navigate(
        AppRoutes.ADD_DEVICE
    )
},
        onSettingsClick = {}
    )
}


            // =====================================================
            // DEVICE DETAIL
            // =====================================================

           composable(
    route = AppRoutes.DEVICE_DETAIL
) { backStackEntry ->

    val deviceId =
        backStackEntry.arguments
            ?.getString("deviceId")
            ?: return@composable

    DeviceDetailScreen(
        deviceId = deviceId,
        deviceViewModel = deviceViewModel,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        onBack = {
            navController.popBackStack()
        },
        onEditDevice = {
            navController.navigate(
                "edit_device/$deviceId"
            )
        },
         onRemoveDevice = {
        deviceViewModel.deleteDevice(deviceId)
    }
    )
}

composable(AppRoutes.ADD_DEVICE) {

    AddDeviceScreen(

        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onDeviceCreated = {
            navController.popBackStack()
        }
    )
}


composable(AppRoutes.FLOORS) {

    FloorsScreen(

        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        deviceViewModel = deviceViewModel,

        onFloorClick = { floorId ->

            navController.navigate(
                "floor_detail/$floorId"
            )
        },

        onAddFloor = {

            navController.navigate(
        AppRoutes.ADD_FLOOR
    )
        },

        onDeleteFloor = { floorId ->

            floorViewModel.deleteFloor(floorId)
        }
    )
}

composable(
    route = AppRoutes.EDIT_ROOM
) { backStackEntry ->

    val roomId =
        backStackEntry.arguments
            ?.getString("roomId")
            ?: return@composable

    EditRoomScreen(
        roomId = roomId,
        roomViewModel = roomViewModel,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onSave = {
            navController.popBackStack()
        },

        onDelete = {
            navController.popBackStack()
        }
    )
}

composable(
    route = AppRoutes.EDIT_DEVICE
) { backStackEntry ->

    val deviceId =
        backStackEntry.arguments
            ?.getString("deviceId")
            ?: return@composable

    EditDeviceScreen(
        deviceId = deviceId,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onSave = {
            navController.popBackStack()
        },

        onDelete = {
            navController.popBackStack()
        }
    )
}

composable(
    route = AppRoutes.ADD_ROOM
) { backStackEntry ->

    val floorId =
        backStackEntry.arguments
            ?.getString("floorId")
            ?: return@composable

    AddRoomScreen(
        floorId = floorId,
        roomViewModel = roomViewModel,

        onBack = {
            navController.popBackStack()
        },

        onRoomCreated = {
            navController.popBackStack()
        }
    )
}

composable(
    route = AppRoutes.ROOM_DETAILS
) { backStackEntry ->

    val roomId =
        backStackEntry.arguments
            ?.getString("roomId")
            ?: return@composable

    RoomDetailsScreen(
        roomId = roomId,
        roomViewModel = roomViewModel,
        floorViewModel = floorViewModel,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onDeviceClick = { deviceId ->

            navController.navigate(
                "device_detail/$deviceId"
            )
        },
          onEditRoom = {
            navController.navigate(
                "edit_room/$roomId"
            )
        },

        onAddDevice = {

            navController.navigate(
                AppRoutes.ADD_DEVICE
            )
        }
    )
}


composable(
    route = AppRoutes.FLOOR_DETAIL
) { backStackEntry ->

    val floorId =
        backStackEntry.arguments?.getString("floorId")
            ?: "ground"

    FloorDetailScreen(
        floorId = floorId,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

      onRoomClick = { roomId ->

    navController.navigate(
        "room_details/$roomId"
    )
},

        onAddRoom = {
    navController.navigate("add_room/$floorId")
},

       onEditFloor = {

    navController.navigate(
        "edit_floor/$floorId"
    )
}
    )
}

composable(
    route = AppRoutes.EDIT_FLOOR
) { backStackEntry ->

    val floorId =
        backStackEntry.arguments
            ?.getString("floorId")
            ?: return@composable

    EditFloorScreen(
        floorId = floorId,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onAddRoom = {
            navController.navigate(
                "add_room/$floorId"
            )
        },

        onRoomClick = { roomId ->

            navController.navigate(
                "room_details/$roomId"
            )
        },

        onSave = {
            navController.popBackStack()
        }
    )
}

composable(AppRoutes.ADD_FLOOR) {

    AddFloorScreen(

        floorViewModel = floorViewModel,
        deviceViewModel = deviceViewModel,

        onBack = {
            navController.popBackStack()
        },

        onAddDevice = {
    navController.navigate(
        AppRoutes.ADD_DEVICE
    )
},

        onCreateFloor = {
            navController.popBackStack()
        }
    )
}
            // =====================================================
            // SETTINGS
            // =====================================================

            composable(AppRoutes.SETTINGS) {

SettingsScreen(
    onAppearanceClick = {
        navController.navigate(AppRoutes.APPEARANCE)
    },
    onAboutClick = {
        navController.navigate(AppRoutes.ABOUT)
    },
     authViewModel = authViewModel


)         
}


            composable(AppRoutes.APPEARANCE) {

    AppearanceScreen(
        onBack = {
            navController.popBackStack()
        }
    )
}

           composable(AppRoutes.ABOUT) {

    AboutScreen(
        onBack = {
            navController.popBackStack()
        }
    )
}


        }
    }
}
@Composable
private fun ElyraBottomBarItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (selected) {
            ElyraTheme.colors.primary
        } else {
            ElyraTheme.colors.surface
        }

    val iconColor =
        if (selected) {
            ElyraTheme.colors.onPrimary
        } else {
            ElyraTheme.colors.textSecondary
        }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(
                horizontal = 18.dp,
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier.size(22.dp),
            tint = iconColor
        )

        if (selected) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = item.label,
                style = ElyraTheme.typography.labelSmall,
                color = iconColor
            )
        }
    }
}