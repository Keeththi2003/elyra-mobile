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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.keeththigan.elyra.feature.devices.DeviceDetailScreen
import com.keeththigan.elyra.feature.devices.DevicesScreen
import com.keeththigan.elyra.feature.devices.AddDeviceScreen
import com.keeththigan.elyra.feature.home.presentation.HomeScreen
import com.keeththigan.elyra.feature.settings.SettingsScreen
import com.keeththigan.elyra.feature.settings.appearance.AppearanceScreen
import com.keeththigan.elyra.feature.settings.about.AboutScreen
import com.keeththigan.elyra.feature.settings.profile.ProfileScreen
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
import com.keeththigan.elyra.feature.settings.appearance.ThemeViewModel
import com.keeththigan.elyra.feature.notifications.NotificationViewModel
import com.keeththigan.elyra.feature.notifications.NotificationsScreen
import com.keeththigan.elyra.feature.reports.ReportsScreen


private object AppRoutes {

    const val HOME = "home"
    const val DEVICES = "devices"
    const val FLOORS = "floors"
    const val SETTINGS = "settings"

const val DEVICE_DETAIL = "device_detail/{deviceId}"
const val FLOOR_DETAIL = "floor_detail/{floorId}"
const val ADD_FLOOR = "add_floor"
const val ADD_DEVICE = "add_device?floorId={floorId}&roomId={roomId}"
const val ADD_ROOM = "add_room/{floorId}"
const val ROOM_DETAILS = "room_details/{roomId}"
const val EDIT_FLOOR = "edit_floor/{floorId}"
const val EDIT_ROOM = "edit_room/{roomId}"
const val EDIT_DEVICE = "edit_device/{deviceId}"
    const val APPEARANCE = "appearance"
    const val ABOUT = "about"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val REPORTS = "reports?deviceId={deviceId}"
}

/**
 * Builds the "add device" route. Passing a floor/room attaches the new device
 * to that room; omitting them creates it unassigned.
 */
private fun addDeviceRoute(
    floorId: String = "",
    roomId: String = ""
): String = "add_device?floorId=$floorId&roomId=$roomId"

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
    roomViewModel: RoomViewModel,
    themeViewModel: ThemeViewModel,
    notificationViewModel: NotificationViewModel
) {

    val navController = rememberNavController()

    /*
     * All forward navigation goes through this helper so repeated taps can
     * never stack duplicate copies of the same destination on the back stack
     * (which is what made "back" require several presses to leave a screen).
     */
    fun navigateTo(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    val navBackStackEntry by
        navController.currentBackStackEntryAsState()

    val currentDestination =
        navBackStackEntry?.destination

    val currentRoute =
        currentDestination?.route

    /*
     * Bottom navigation stays visible while browsing — the tabs, plus the
     * detail screens you reach from them — so you can always jump sections.
     *
     * It is hidden only on focused create/edit flows, where the task should
     * be finished or cancelled rather than abandoned sideways.
     */
    val isFocusedTask =
        currentRoute?.let { route ->
            route.startsWith("add_") || route.startsWith("edit_")
        } ?: false

    val showBottomBar = !isFocusedTask

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

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val notificationState by
        notificationViewModel.state.collectAsStateWithLifecycle()

    HomeScreen(

        deviceViewModel = deviceViewModel,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        userName = authState.user?.name.orEmpty(),
        unreadAlertCount = notificationState.unreadCount,

        onFloorClick = { floorId ->

            navigateTo(
                "floor_detail/$floorId"
            )
        },

        onDeviceClick = { deviceId ->

            navigateTo(
                "device_detail/$deviceId"
            )
        },

        onAddFloor = {

            navigateTo(
                AppRoutes.ADD_FLOOR
            )
        },

        onAddDevice = {

            navigateTo(addDeviceRoute())
        },

        onAlertsClick = {
            navigateTo(AppRoutes.NOTIFICATIONS)
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

            navigateTo(
                "device_detail/$deviceId"
            )
        },
        onAddDevice = {
    navigateTo(addDeviceRoute())
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
            navigateTo(
                "edit_device/$deviceId"
            )
        },
         onRemoveDevice = {
        deviceViewModel.deleteDevice(deviceId)
    },
        onViewReport = {
            navigateTo("reports?deviceId=$deviceId")
        }
    )
}

composable(
    route = AppRoutes.ADD_DEVICE,
    arguments = listOf(
        navArgument("floorId") {
            type = NavType.StringType
            defaultValue = ""
        },
        navArgument("roomId") {
            type = NavType.StringType
            defaultValue = ""
        }
    )
) { backStackEntry ->

    AddDeviceScreen(

        deviceViewModel = deviceViewModel,

        // When "Add device" is opened from inside a room, the new device is
        // attached to that room instead of being created unassigned.
        floorId = backStackEntry.arguments
            ?.getString("floorId").orEmpty(),

        roomId = backStackEntry.arguments
            ?.getString("roomId").orEmpty(),

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

            navigateTo(
                "floor_detail/$floorId"
            )
        },

        onAddFloor = {

            navigateTo(
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

            navigateTo(
                "device_detail/$deviceId"
            )
        },
          onEditRoom = {
            navigateTo(
                "edit_room/$roomId"
            )
        },

        onAddDevice = { deviceFloorId, deviceRoomId ->

            navigateTo(
                addDeviceRoute(
                    floorId = deviceFloorId,
                    roomId = deviceRoomId
                )
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

    navigateTo(
        "room_details/$roomId"
    )
},

        onDeviceClick = { deviceId ->
            navigateTo("device_detail/$deviceId")
        },

        onDeleteFloor = {
            floorViewModel.deleteFloor(floorId)
            navController.popBackStack()
        },

        onAddRoom = {
    navigateTo("add_room/$floorId")
},

       onEditFloor = {

    navigateTo(
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
            navigateTo(
                "add_room/$floorId"
            )
        },

        onRoomClick = { roomId ->

            navigateTo(
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
    navigateTo(addDeviceRoute())
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

val notificationState by
    notificationViewModel.state.collectAsStateWithLifecycle()

SettingsScreen(
    notificationsEnabled = notificationState.notificationsEnabled,
    onNotificationsEnabledChange = {
        notificationViewModel.setNotificationsEnabled(it)
    },
    onAlertsClick = {
        navigateTo(AppRoutes.NOTIFICATIONS)
    },
    onReportsClick = {
        navigateTo("reports?deviceId=")
    },
    onProfileClick = {
        navigateTo(AppRoutes.PROFILE)
    },
    onAppearanceClick = {
        navigateTo(AppRoutes.APPEARANCE)
    },
    onAboutClick = {
        navigateTo(AppRoutes.ABOUT)
    },
     authViewModel = authViewModel


)         
}


            composable(
    route = AppRoutes.REPORTS,
    arguments = listOf(
        navArgument("deviceId") {
            type = NavType.StringType
            defaultValue = ""
        }
    )
) { backStackEntry ->

    ReportsScreen(
        deviceId = backStackEntry.arguments
            ?.getString("deviceId").orEmpty(),
        deviceViewModel = deviceViewModel,
        roomViewModel = roomViewModel,
        onDeviceClick = { deviceId ->
            navigateTo("device_detail/$deviceId")
        },
        onBack = {
            navController.popBackStack()
        }
    )
}

            composable(AppRoutes.NOTIFICATIONS) {

    NotificationsScreen(
        notificationViewModel = notificationViewModel,
        onBack = {
            navController.popBackStack()
        }
    )
}

            composable(AppRoutes.PROFILE) {

    ProfileScreen(
        authViewModel = authViewModel,
        deviceViewModel = deviceViewModel,
        floorViewModel = floorViewModel,
        roomViewModel = roomViewModel,
        onBack = {
            navController.popBackStack()
        }
    )
}

            composable(AppRoutes.APPEARANCE) {

    val appearance by
        themeViewModel.appearance.collectAsStateWithLifecycle()

    AppearanceScreen(
        selectedOption = appearance,
        onOptionSelected = { option ->
            themeViewModel.setAppearance(option)
        },
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