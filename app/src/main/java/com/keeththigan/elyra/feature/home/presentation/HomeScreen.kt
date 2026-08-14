package com.keeththigan.elyra.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme


// ============================================================================
// HOME DEVICE
// ============================================================================

private data class HomeDevice(
    val id: String,
    val name: String,
    val type: String,
    val isOn: Boolean
)


// ============================================================================
// HOME FLOOR
// ============================================================================

private data class HomeFloor(
    val id: String,
    val name: String,
    val roomCount: Int,
    val deviceCount: Int
)


// ============================================================================
// HOME SCREEN
// ============================================================================

@Composable
fun HomeScreen(
    onFloorClick: (String) -> Unit,
    onDeviceClick: (String) -> Unit,
    onAddFloor: () -> Unit,
    onAddDevice: () -> Unit,
    onProfileClick: () -> Unit
) {

    val devices = remember {

        listOf(
            HomeDevice(
                id = "living_room_light",
                name = "Living Room Light",
                type = "Light",
                isOn = true
            ),

            HomeDevice(
                id = "kitchen_outlet",
                name = "Kitchen Outlet",
                type = "Outlet",
                isOn = false
            ),

            HomeDevice(
                id = "kitchen_switch",
                name = "Kitchen Switch",
                type = "Switch",
                isOn = true
            ),

            HomeDevice(
                id = "bedroom_ac",
                name = "Bedroom AC",
                type = "AC",
                isOn = false
            )
        )
    }

    val floors = remember {

        listOf(
            HomeFloor(
                id = "ground",
                name = "Ground Floor",
                roomCount = 3,
                deviceCount = 5
            ),

            HomeFloor(
                id = "first",
                name = "First Floor",
                roomCount = 2,
                deviceCount = 4
            )
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
    ) {

        // ====================================================================
        // TOP BAR
        // ====================================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Good evening",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Welcome to Elyra",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )
            }

            // Profile button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.surface
                    )
                    .clickable(
                        onClick = onProfileClick
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp),
                    tint = ElyraTheme.colors.textPrimary
                )
            }
        }


        // ====================================================================
        // MAIN CONTENT
        // ====================================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // =================================================================
            // DEVICES SECTION
            // =================================================================

            item {

                SectionHeader(
                    title = "Devices",
                    subtitle = "Your recently added devices",
                    onAddClick = onAddDevice
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    userScrollEnabled = false
                ) {

                    items(
                        items = devices.take(4),
                        key = {
                            it.id
                        }
                    ) { device ->

                        DeviceCard(
                            device = device,
                            onClick = {
                                onDeviceClick(device.id)
                            }
                        )
                    }
                }
            }


            // =================================================================
            // FLOORS SECTION
            // =================================================================

            item {

                SectionHeader(
                    title = "Floors",
                    subtitle = "Manage your home spaces",
                    onAddClick = onAddFloor
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    floors.forEach { floor ->

                        FloorCard(
                            floor = floor,
                            onClick = {
                                onFloorClick(floor.id)
                            }
                        )
                    }
                }
            }
        }
    }
}


// ============================================================================
// SECTION HEADER
// ============================================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    onAddClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = subtitle,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }


        // ================================================================
        // ADD BUTTON
        // ================================================================

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.primary
                )
                .clickable(
                    onClick = onAddClick
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add $title",
                modifier = Modifier.size(21.dp),
                tint = ElyraTheme.colors.onPrimary
            )
        }
    }
}


// ============================================================================
// DEVICE CARD
// ============================================================================

@Composable
private fun DeviceCard(
    device: HomeDevice,
    onClick: () -> Unit
) {

    val activeColor = Color(0xFF22C55E)

    val iconColor =
        if (device.isOn) {
            activeColor
        } else {
            ElyraTheme.colors.textSecondary
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(
                        RoundedCornerShape(13.dp)
                    )
                    .background(
                        iconColor.copy(alpha = 0.10f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = homeDeviceIcon(device.type),
                    contentDescription = null,
                    modifier = Modifier.size(21.dp),
                    tint = iconColor
                )
            }


            Spacer(
                modifier = Modifier.weight(1f)
            )


            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (device.isOn) {
                            activeColor
                        } else {
                            ElyraTheme.colors.textTertiary
                        }
                    )
            )
        }

        Spacer(
            modifier = Modifier.height(13.dp)
        )

        Text(
            text = device.name,
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary,
            maxLines = 1
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = if (device.isOn) "On" else "Off",
            style = ElyraTheme.typography.bodySmall,
            color =
                if (device.isOn) {
                    activeColor
                } else {
                    ElyraTheme.colors.textSecondary
                }
        )
    }
}


// ============================================================================
// FLOOR CARD
// ============================================================================

@Composable
private fun FloorCard(
    floor: HomeFloor,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.size(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = floor.name,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${floor.roomCount} rooms · ${floor.deviceCount} devices",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Open ${floor.name}",
            modifier = Modifier.size(19.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// DEVICE ICON
// ============================================================================

private fun homeDeviceIcon(
    type: String
): ImageVector {

    return when (type) {

        "Light" ->
            Icons.Outlined.Lightbulb

        "Outlet" ->
            Icons.Outlined.Power

        "Switch" ->
            Icons.Outlined.Devices

        "AC" ->
            Icons.Outlined.AcUnit

        "Camera" ->
            Icons.Outlined.CameraAlt

        else ->
            Icons.Outlined.Devices
    }
}