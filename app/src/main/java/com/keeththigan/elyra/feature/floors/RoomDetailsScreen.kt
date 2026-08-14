package com.keeththigan.elyra.feature.floors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

// ============================================================================
// TEMPORARY UI MODEL
// ============================================================================
// This is only for displaying the UI until the repository/database is connected.
// Do NOT pass these values through navigation.
//
// Later:
// roomId -> ViewModel -> Repository -> Database
// ============================================================================

private data class RoomDeviceUi(
    val id: String,
    val name: String,
    val type: String,
    val isOn: Boolean
)

@Composable
fun RoomDetailsScreen(
    roomId: String,
    onBack: () -> Unit,
    onDeviceClick: (String) -> Unit,
    onAddDevice: () -> Unit
) {

    /*
     * Later this data will come from:
     *
     * roomId
     *   ↓
     * ViewModel
     *   ↓
     * Repository
     *   ↓
     * Database
     *
     * Do not pass roomName/devices through navigation.
     */

    val roomName = remember {
        "Living Room"
    }

    val floorName = remember {
        "Ground Floor"
    }

    val devices = remember {

        listOf(

            RoomDeviceUi(
                id = "device_001",
                name = "Living Room Light",
                type = "Light",
                isOn = true
            ),

            RoomDeviceUi(
                id = "device_002",
                name = "Smart Outlet",
                type = "Outlet",
                isOn = false
            ),

            RoomDeviceUi(
                id = "device_003",
                name = "Ceiling Fan",
                type = "Multi Switch",
                isOn = true
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
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        ElyraTheme.colors.surface
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = ElyraTheme.colors.textPrimary
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = roomName,
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = floorName,
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        // ====================================================================
        // CONTENT
        // ====================================================================

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // =================================================================
            // ROOM HEADER
            // =================================================================

            item {

                RoomSummaryCard(
                    roomName = roomName,
                    floorName = floorName,
                    deviceCount = devices.size
                )
            }

            // =================================================================
            // DEVICE SECTION HEADER
            // =================================================================

            item {

                Column {

                    Text(
                        text = "Devices",
                        style = ElyraTheme.typography.titleMedium,
                        color = ElyraTheme.colors.textPrimary
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "${devices.size} devices connected to this room",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }

            // =================================================================
            // DEVICE LIST
            // =================================================================

            if (devices.isEmpty()) {

                item {

                    EmptyRoomDevices(
                        onAddDevice = onAddDevice
                    )
                }

            } else {

                items(
                    count = devices.size
                ) { index ->

                    val device = devices[index]

                    RoomDeviceCard(
                        device = device,
                        onClick = {
                            onDeviceClick(device.id)
                        }
                    )
                }
            }

            // =================================================================
            // ADD DEVICE
            // =================================================================

            item {

                AddDeviceCard(
                    onClick = onAddDevice
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}


// ============================================================================
// ROOM SUMMARY
// ============================================================================

@Composable
private fun RoomSummaryCard(
    roomName: String,
    floorName: String,
    deviceCount: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(
                    RoundedCornerShape(17.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = roomName,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = floorName,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "$deviceCount devices",
                style = ElyraTheme.typography.labelSmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// DEVICE CARD
// ============================================================================

@Composable
private fun RoomDeviceCard(
    device: RoomDeviceUi,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 15.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        DeviceIcon(
            type = device.type
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = device.type,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        // ON / OFF

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (device.isOn) {
                        ElyraTheme.colors.success.copy(
                            alpha = 0.12f
                        )
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                )
                .padding(
                    horizontal = 9.dp,
                    vertical = 5.dp
                )
        ) {

            Text(
                text = if (device.isOn) "ON" else "OFF",
                style = ElyraTheme.typography.labelSmall,
                color =
                    if (device.isOn) {
                        ElyraTheme.colors.success
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Open device",
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// DEVICE ICON
// ============================================================================

@Composable
private fun DeviceIcon(
    type: String
) {

    val icon: ImageVector =
        when (type.lowercase()) {

            "light" ->
                Icons.Outlined.Lightbulb

            "outlet" ->
                Icons.Outlined.Power

            "multi switch" ->
                Icons.Outlined.Tune

            else ->
                Icons.Outlined.Power
        }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(
                RoundedCornerShape(13.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(21.dp),
            tint = ElyraTheme.colors.textPrimary
        )
    }
}


// ============================================================================
// EMPTY DEVICES
// ============================================================================

@Composable
private fun EmptyRoomDevices(
    onAddDevice: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.surface
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "No devices yet",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Add a device to start managing this room.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        AddDeviceCard(
            onClick = onAddDevice
        )
    }
}


// ============================================================================
// ADD DEVICE
// ============================================================================

@Composable
private fun AddDeviceCard(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 15.dp,
                vertical = 13.dp
            )
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.primary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ElyraTheme.colors.onPrimary
            )
        }

        Spacer(
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Add device",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "Connect a device to this room",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(19.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}