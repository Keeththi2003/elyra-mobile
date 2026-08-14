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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

// ================================================================
// DEVICE MODEL
// ================================================================

private data class DeviceUiModel(
    val name: String,
    val room: String,
    val type: String,
    val isOn: Boolean,
    val isOnline: Boolean,
    val icon: ImageVector
)


// ================================================================
// DEVICES SCREEN
// ================================================================

@Composable
fun DevicesScreen(
    onBack: () -> Unit = {},
    onDeviceClick: (String) -> Unit = {}
) {

    val devices = listOf(

        DeviceUiModel(
            name = "Living Room Light",
            room = "Living Room",
            type = "Smart Light",
            isOn = true,
            isOnline = true,
            icon = Icons.Outlined.Lightbulb
        ),

        DeviceUiModel(
            name = "Kitchen Outlet",
            room = "Kitchen",
            type = "Smart Outlet",
            isOn = false,
            isOnline = true,
            icon = Icons.Outlined.Power
        ),

        DeviceUiModel(
            name = "Bedroom Fan",
            room = "Bedroom",
            type = "Smart Fan",
            isOn = true,
            isOnline = true,
            icon = Icons.Outlined.Air
        ),

        DeviceUiModel(
            name = "Office Light",
            room = "Office",
            type = "Smart Light",
            isOn = false,
            isOnline = false,
            icon = Icons.Outlined.Lightbulb
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                )
        ) {

            Text(
                text = "Devices",
                style = ElyraTheme.typography.displaySmall,
                color = ElyraTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Control everything in your home.",
                style = ElyraTheme.typography.bodyLarge,
                color = ElyraTheme.colors.textSecondary
            )
        }

        // ========================================================
        // SUMMARY
        // ========================================================

        DeviceSummary(
            devices = devices
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ========================================================
        // DEVICE LIST
        // ========================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "All devices",
                        style = ElyraTheme.typography.titleLarge,
                        color = ElyraTheme.colors.textPrimary
                    )

                    Text(
                        text = "${devices.size}",
                        style = ElyraTheme.typography.labelLarge,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }

            items(
                items = devices,
                key = { it.name }
            ) { device ->

                DeviceListItem(
                    device = device,
                    onClick = {
                        onDeviceClick(device.name)
                    }
                )
            }
        }
    }
}


// ================================================================
// SUMMARY
// ================================================================

@Composable
private fun DeviceSummary(
    devices: List<DeviceUiModel>
) {

    val activeCount =
        devices.count {
            it.isOn && it.isOnline
        }

    val offlineCount =
        devices.count {
            !it.isOnline
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        SummaryItem(
            modifier = Modifier.weight(1f),
            value = devices.size.toString(),
            label = "Devices"
        )

        SummaryItem(
            modifier = Modifier.weight(1f),
            value = activeCount.toString(),
            label = "Active",
            valueColor = ElyraTheme.colors.success
        )

        SummaryItem(
            modifier = Modifier.weight(1f),
            value = offlineCount.toString(),
            label = "Offline",
            valueColor =
                if (offlineCount > 0) {
                    ElyraTheme.colors.error
                } else {
                    ElyraTheme.colors.textPrimary
                }
        )
    }
}


// ================================================================
// SUMMARY ITEM
// ================================================================

@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor :Color = ElyraTheme.colors.textPrimary
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surfaceSecondary)
            .padding(16.dp)
    ) {

        Text(
            text = value,
            style = ElyraTheme.typography.headlineMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ================================================================
// DEVICE LIST ITEM
// ================================================================

@Composable
private fun DeviceListItem(
    device: DeviceUiModel,
    onClick: () -> Unit
) {

    val iconBackground =
        when {
            !device.isOnline ->
                ElyraTheme.colors.errorContainer

            device.isOn ->
                ElyraTheme.colors.successContainer

            else ->
                ElyraTheme.colors.surfaceInteractive
        }

    val iconColor =
        when {
            !device.isOnline ->
                ElyraTheme.colors.error

            device.isOn ->
                ElyraTheme.colors.success

            else ->
                ElyraTheme.colors.textSecondary
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(ElyraTheme.colors.surfaceSecondary)
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ========================================================
        // DEVICE ICON
        // ========================================================

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = device.icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        // ========================================================
        // INFORMATION
        // ========================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "${device.room} • ${device.type}",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                !device.isOnline ->
                                    ElyraTheme.colors.error

                                device.isOn ->
                                    ElyraTheme.colors.success

                                else ->
                                    ElyraTheme.colors.textTertiary
                            }
                        )
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = when {
                        !device.isOnline -> "Offline"
                        device.isOn -> "On"
                        else -> "Off"
                    },
                    style = ElyraTheme.typography.labelMedium,
                    color = when {
                        !device.isOnline ->
                            ElyraTheme.colors.error

                        device.isOn ->
                            ElyraTheme.colors.success

                        else ->
                            ElyraTheme.colors.textSecondary
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        // ========================================================
        // STATUS CONTROL
        // ========================================================

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (device.isOn && device.isOnline) {
                        ElyraTheme.colors.primary
                    } else {
                        ElyraTheme.colors.background
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector =
                    if (!device.isOnline) {
                        Icons.Outlined.WifiOff
                    } else {
                        Icons.Outlined.Tune
                    },
                contentDescription = null,
                tint =
                    if (device.isOn && device.isOnline) {
                        ElyraTheme.colors.onPrimary
                    } else {
                        ElyraTheme.colors.textSecondary
                    },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}