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
import com.keeththigan.elyra.feature.home.presentation.HomeScreen
import com.keeththigan.elyra.feature.settings.SettingsScreen
import com.keeththigan.elyra.core.designsystem.ElyraTheme

private object AppRoutes {

    const val HOME = "home"
    const val DEVICES = "devices"
    const val SETTINGS = "settings"

    const val DEVICE_DETAIL = "device_detail/{deviceName}"
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
        route = AppRoutes.SETTINGS,
        label = "Settings",
        icon = Icons.Outlined.Settings
    )
)

@Composable
fun AppNavigation() {

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

                    onFloorClick = { floorId ->

                        // Floor screen later.
                    },

                    onDeviceClick = { deviceId ->

                        /*
                         * For now, clicking a device
                         * from Home opens Devices.
                         */
                        navController.navigate(
                            AppRoutes.DEVICES
                        )
                    },

                    onAddFloor = {

                        // Add Floor screen later.
                    },

                    onSettingsClick = {

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

                    onDeviceClick = { deviceName ->

                        /*
                         * Open device details.
                         *
                         * We will improve the argument handling
                         * later when the real Device model exists.
                         */
                        navController.navigate(
                            "device_detail/$deviceName"
                        )
                    }
                )
            }


            // =====================================================
            // DEVICE DETAIL
            // =====================================================

            composable(
                route = AppRoutes.DEVICE_DETAIL
            ) { backStackEntry ->

                val deviceName =
                    backStackEntry.arguments
                        ?.getString("deviceName")
                        ?: "Device"

                DeviceDetailScreen(

                    deviceName = deviceName,

                    onBack = {

                        navController.popBackStack()
                    }
                )
            }


            // =====================================================
            // SETTINGS
            // =====================================================

            composable(AppRoutes.SETTINGS) {

                SettingsScreen()
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