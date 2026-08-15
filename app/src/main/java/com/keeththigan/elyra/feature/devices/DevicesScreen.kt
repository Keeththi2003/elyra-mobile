package com.keeththigan.elyra.feature.devices

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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


@Composable
fun DevicesScreen(
    onDeviceClick: (String) -> Unit,
    onAddDevice: () -> Unit,
    onSettingsClick: () -> Unit
) {

    /*
     * Temporary UI data.
     *
     * Later this list will come from the repository/database.
     */
    val devices = remember {

        listOf(

            Device(
                id = "device_001",
                name = "Living Room Light",
                type = DeviceType.LIGHT,
                status = DeviceStatus.ON,
                brightness = 80
            ),

            Device(
                id = "device_002",
                name = "Kitchen Outlet",
                type = DeviceType.OUTLET,
                status = DeviceStatus.OFF
            ),

            Device(
                id = "device_003",
                name = "Bedroom Switch",
                type = DeviceType.MULTI_SWITCH,
                status = DeviceStatus.ON,
                switchCount = 3
            ),

            Device(
                id = "device_004",
                name = "Bedroom Iron",
                type = DeviceType.SAFETY_APPLIANCE,
                status = DeviceStatus.OFF,
                maxOnDurationMinutes = 30
            ),

            Device(
                id = "device_005",
                name = "Front Door Camera",
                type = DeviceType.SECURITY_CAMERA,
                status = DeviceStatus.ON
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

        // ================================================================
        // HEADER
        // ================================================================

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
                    text = "Devices",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "${devices.size} devices in your home",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            IconButton(
                onClick = onAddDevice,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.primary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add device",
                    tint = ElyraTheme.colors.onPrimary
                )
            }
        }


        // ================================================================
        // DEVICE LIST
        // ================================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                DeviceSummaryCard(
                    devices = devices
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            items(
                items = devices,
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

            item {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}


// ============================================================================
// SUMMARY
// ============================================================================

@Composable
private fun DeviceSummaryCard(
    devices: List<Device>
) {

    val online =
        devices.count {
            it.status == DeviceStatus.ON
        }

    val offline =
        devices.count {
            it.status == DeviceStatus.OFF
        }

    val problems =
        devices.count {
            it.status == DeviceStatus.ERROR ||
                    it.status == DeviceStatus.DISCONNECTED
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(
                vertical = 18.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        DeviceSummaryItem(
            value = online.toString(),
            label = "Online",
            color = Color(0xFF22C55E)
        )

        DeviceSummaryItem(
            value = offline.toString(),
            label = "Off",
            color = ElyraTheme.colors.textSecondary
        )

        DeviceSummaryItem(
            value = problems.toString(),
            label = "Issues",
            color = Color(0xFFEF4444)
        )
    }
}


@Composable
private fun DeviceSummaryItem(
    value: String,
    label: String,
    color: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            style = ElyraTheme.typography.titleLarge,
            color = color
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// DEVICE CARD
// ============================================================================

@Composable
private fun DeviceCard(
    device: Device,
    onClick: () -> Unit
) {

    val icon =
        when (device.type) {

            DeviceType.LIGHT ->
                Icons.Outlined.Lightbulb

            DeviceType.OUTLET ->
                Icons.Outlined.Power

            DeviceType.MULTI_SWITCH ->
                Icons.Outlined.Tune

            DeviceType.SAFETY_APPLIANCE ->
                Icons.Outlined.WarningAmber

            DeviceType.SECURITY_CAMERA ->
                Icons.Outlined.CameraAlt
        }

    val statusColor =
        when (device.status) {

            DeviceStatus.ON ->
                Color(0xFF22C55E)

            DeviceStatus.OFF ->
                ElyraTheme.colors.textSecondary

            DeviceStatus.ERROR ->
                Color(0xFFEF4444)

            DeviceStatus.DISCONNECTED ->
                Color(0xFFF59E0B)
        }

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
                horizontal = 14.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ---------------------------------------------------------------
        // ICON
        // ---------------------------------------------------------------

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(
                    statusColor.copy(
                        alpha = 0.10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(23.dp),
                tint = statusColor
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        // ---------------------------------------------------------------
        // INFORMATION
        // ---------------------------------------------------------------

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
                text = device.type.displayName(),
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        // ---------------------------------------------------------------
        // STATUS
        // ---------------------------------------------------------------

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = device.status.displayName(),
                style = ElyraTheme.typography.labelMedium,
                color = statusColor
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Device options",
                modifier = Modifier.size(19.dp),
                tint = ElyraTheme.colors.textTertiary
            )
        }
    }
}


// ============================================================================
// DISPLAY HELPERS
// ============================================================================

private fun DeviceType.displayName(): String {

    return when (this) {

        DeviceType.LIGHT ->
            "Light"

        DeviceType.OUTLET ->
            "Electrical Outlet"

        DeviceType.MULTI_SWITCH ->
            "Multi-Switch"

        DeviceType.SAFETY_APPLIANCE ->
            "Safety Appliance"

        DeviceType.SECURITY_CAMERA ->
            "Security Camera"
    }
}


private fun DeviceStatus.displayName(): String {

    return when (this) {

        DeviceStatus.ON ->
            "ON"

        DeviceStatus.OFF ->
            "OFF"

        DeviceStatus.ERROR ->
            "ERROR"

        DeviceStatus.DISCONNECTED ->
            "DISCONNECTED"
    }
}