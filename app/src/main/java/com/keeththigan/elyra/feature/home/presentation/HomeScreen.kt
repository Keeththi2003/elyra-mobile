package com.keeththigan.elyra.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel


// ============================================================================
// HOME SCREEN
// ============================================================================

@Composable
fun HomeScreen(
    deviceViewModel: DeviceViewModel,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel,
    userName: String = "",
    unreadAlertCount: Int = 0,
    onFloorClick: (String) -> Unit,
    onDeviceClick: (String) -> Unit,
    onAddFloor: () -> Unit,
    onAddDevice: () -> Unit,
    onAlertsClick: () -> Unit
) {

    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()
    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        deviceViewModel.loadDevices()
        floorViewModel.loadFloors()
        roomViewModel.loadRooms()
    }

    val devices = deviceState.devices
    val floors = floorState.floors

    val activeCount = devices.count { it.status == DeviceStatus.ON }

    val attentionCount =
        devices.count {
            it.status == DeviceStatus.ERROR ||
                it.status == DeviceStatus.DISCONNECTED
        }

    val error =
        deviceState.error ?: floorState.error ?: roomState.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        // ====================================================================
        // GREETING
        // ====================================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = greeting(),
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = userName.takeIf { it.isNotBlank() }
                        ?.let { "$it's home" }
                        ?: "Your home",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.textPrimary
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ElyraTheme.colors.surface)
                    .clickable(onClick = onAlertsClick),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.NotificationsNone,
                    contentDescription = "Alerts",
                    modifier = Modifier.size(21.dp),
                    tint = ElyraTheme.colors.textPrimary
                )

                if (unreadAlertCount > 0) {

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 9.dp, end = 9.dp)
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(ElyraTheme.colors.error)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            if (!deviceState.isOnline) {

                item {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(ElyraTheme.colors.warningContainer)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ElyraTheme.colors.warning)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "You're offline — controls are disabled",
                            style = ElyraTheme.typography.bodyMedium,
                            color = ElyraTheme.colors.onWarningContainer
                        )
                    }
                }
            }

            if (error != null) {

                item {
                    Text(
                        text = error,
                        style = ElyraTheme.typography.bodyMedium,
                        color = ElyraTheme.colors.error
                    )
                }
            }

            // ================================================================
            // AT-A-GLANCE SUMMARY
            // ================================================================

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    SummaryTile(
                        value = activeCount.toString(),
                        label = if (activeCount == 1) "Device on" else "Devices on",
                        modifier = Modifier.weight(1f)
                    )

                    SummaryTile(
                        value = devices.size.toString(),
                        label = "Total",
                        modifier = Modifier.weight(1f)
                    )

                    SummaryTile(
                        value = attentionCount.toString(),
                        label = "Need attention",
                        emphasise = attentionCount > 0,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ================================================================
            // QUICK CONTROLS
            // ================================================================

            item {

                SectionHeader(
                    title = "Quick controls",
                    subtitle = if (devices.isEmpty()) {
                        "Add a device to get started"
                    } else {
                        "Toggle without leaving home"
                    },
                    actionLabel = "Add",
                    onActionClick = onAddDevice
                )
            }

            if (devices.isEmpty()) {

                item {
                    EmptyState(
                        icon = Icons.Outlined.Devices,
                        title = "No devices yet",
                        message = "Add your first device to start controlling " +
                            "your home from here.",
                        actionLabel = "Add device",
                        onAction = onAddDevice
                    )
                }

            } else {

                // Four most recent devices, laid out 2x2. Built from plain
                // rows rather than a nested lazy grid so it measures cleanly
                // inside this LazyColumn.
                items(
                    items = devices.take(4).chunked(2),
                    key = { row -> row.first().id }
                ) { row ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        row.forEach { device ->

                            QuickControlTile(
                                device = device,
                                isOnline = deviceState.isOnline,
                                modifier = Modifier.weight(1f),
                                onClick = { onDeviceClick(device.id) },
                                onToggle = {
                                    deviceViewModel.toggleDevice(device.id, it)
                                }
                            )
                        }

                        // Keeps a lone tile at half width on an odd last row.
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // ================================================================
            // FLOORS
            // ================================================================

            item {

                SectionHeader(
                    title = "Floors",
                    subtitle = if (floors.isEmpty()) {
                        "Organise your home by floor"
                    } else {
                        "${floors.size} floor${if (floors.size == 1) "" else "s"}"
                    },
                    actionLabel = "Add",
                    onActionClick = onAddFloor
                )
            }

            if (floors.isEmpty()) {

                item {
                    EmptyState(
                        icon = Icons.Outlined.Apartment,
                        title = "No floors yet",
                        message = "Create a floor, then add rooms and devices " +
                            "inside it.",
                        actionLabel = "Add floor",
                        onAction = onAddFloor
                    )
                }

            } else {

                items(
                    items = floors,
                    key = { it.id }
                ) { floor ->

                    FloorRow(
                        name = floor.name,
                        roomCount = roomState.rooms.count {
                            it.floorId == floor.id
                        },
                        deviceCount = devices.count {
                            it.floorId == floor.id
                        },
                        onClick = { onFloorClick(floor.id) }
                    )
                }
            }
        }
    }
}


// ============================================================================
// SUMMARY TILE
// ============================================================================

@Composable
private fun SummaryTile(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    emphasise: Boolean = false
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {

        Text(
            text = value,
            style = ElyraTheme.typography.headlineMedium,
            color = if (emphasise) {
                ElyraTheme.colors.error
            } else {
                ElyraTheme.colors.textPrimary
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary,
            maxLines = 2
        )
    }
}


// ============================================================================
// SECTION HEADER
// ============================================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = title,
                style = ElyraTheme.typography.titleLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(ElyraTheme.colors.surface)
                .border(
                    width = 1.dp,
                    color = ElyraTheme.colors.borderSubtle,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(onClick = onActionClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = ElyraTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = actionLabel,
                style = ElyraTheme.typography.labelMedium,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}


// ============================================================================
// QUICK CONTROL ROW
// ============================================================================

@Composable
private fun QuickControlTile(
    device: Device,
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit
) {

    val isOn = device.status == DeviceStatus.ON

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(ElyraTheme.colors.surfaceSecondary),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = homeDeviceIcon(device.type),
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = if (isOn) {
                        ElyraTheme.colors.textPrimary
                    } else {
                        ElyraTheme.colors.textTertiary
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = isOn,
                onCheckedChange = onToggle,
                enabled = device.isControllable && isOnline,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElyraTheme.colors.onPrimary,
                    checkedTrackColor = ElyraTheme.colors.primary,
                    uncheckedThumbColor = ElyraTheme.colors.textTertiary,
                    uncheckedTrackColor = ElyraTheme.colors.surfaceInteractive
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = device.name,
            style = ElyraTheme.typography.bodyLarge,
            color = ElyraTheme.colors.textPrimary,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = device.status.homeLabel(),
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary,
            maxLines = 1
        )
    }
}


// ============================================================================
// FLOOR ROW
// ============================================================================

@Composable
private fun FloorRow(
    name: String,
    roomCount: Int,
    deviceCount: Int,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = name,
                style = ElyraTheme.typography.bodyLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$roomCount room${if (roomCount == 1) "" else "s"} · " +
                    "$deviceCount device${if (deviceCount == 1) "" else "s"}",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// EMPTY STATE
// ============================================================================

@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = title,
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = message,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(ElyraTheme.colors.primary)
                .clickable(onClick = onAction)
                .padding(horizontal = 22.dp, vertical = 12.dp)
        ) {

            Text(
                text = actionLabel,
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.onPrimary
            )
        }
    }
}


// ============================================================================
// HELPERS
// ============================================================================

private fun greeting(): String {

    val hour =
        java.util.Calendar.getInstance()
            .get(java.util.Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Good night"
    }
}


private fun DeviceStatus.homeLabel(): String =
    when (this) {
        DeviceStatus.ON -> "On"
        DeviceStatus.OFF -> "Off"
        DeviceStatus.ERROR -> "Error reported"
        DeviceStatus.DISCONNECTED -> "Disconnected"
    }


private fun homeDeviceIcon(
    type: DeviceType
): ImageVector =
    when (type) {
        DeviceType.LIGHT -> Icons.Outlined.Lightbulb
        DeviceType.OUTLET -> Icons.Outlined.Power
        DeviceType.MULTI_SWITCH -> Icons.Outlined.Tune
        DeviceType.SAFETY_APPLIANCE -> Icons.Outlined.Security
        DeviceType.SECURITY_CAMERA -> Icons.Outlined.CameraAlt
    }
