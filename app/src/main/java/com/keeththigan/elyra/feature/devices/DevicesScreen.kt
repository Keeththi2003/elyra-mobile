package com.keeththigan.elyra.feature.devices

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
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Lightbulb
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun DevicesScreen(
    deviceViewModel: DeviceViewModel,
    onDeviceClick: (String) -> Unit,
    onAddDevice: () -> Unit,
    onSettingsClick: () -> Unit
) {

    val state by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        deviceViewModel.loadDevices()
    }

    var filter by remember { mutableStateOf(DeviceFilter.ALL) }

    val devices = state.devices

    val visibleDevices = when (filter) {
        DeviceFilter.ALL -> devices
        DeviceFilter.ON -> devices.filter { it.status == DeviceStatus.ON }
        DeviceFilter.ISSUES -> devices.filter {
            it.status == DeviceStatus.ERROR ||
                it.status == DeviceStatus.DISCONNECTED
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Devices",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${devices.size} device${if (devices.size == 1) "" else "s"} " +
                        "· ${devices.count { it.status == DeviceStatus.ON }} on",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ElyraTheme.colors.primary)
                    .clickable(onClick = onAddDevice),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add device",
                    modifier = Modifier.size(21.dp),
                    tint = ElyraTheme.colors.onPrimary
                )
            }
        }

        if (devices.isNotEmpty()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                DeviceFilter.entries.forEach { option ->

                    FilterChip(
                        label = option.label,
                        selected = option == filter,
                        onClick = { filter = option }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (state.error != null) {

                item {
                    Text(
                        text = state.error ?: "",
                        style = ElyraTheme.typography.bodyMedium,
                        color = ElyraTheme.colors.error
                    )
                }
            }

            if (devices.isEmpty()) {

                item {
                    EmptyDevices(onAddDevice = onAddDevice)
                }

            } else if (visibleDevices.isEmpty()) {

                item {
                    Text(
                        text = "Nothing matches this filter.",
                        style = ElyraTheme.typography.bodyMedium,
                        color = ElyraTheme.colors.textSecondary
                    )
                }

            } else {

                items(
                    items = visibleDevices,
                    key = { it.id }
                ) { device ->

                    DeviceRow(
                        device = device,
                        onClick = { onDeviceClick(device.id) },
                        onToggle = {
                            deviceViewModel.toggleDevice(device.id, it)
                        }
                    )
                }
            }
        }
    }
}

private enum class DeviceFilter(val label: String) {
    ALL("All"),
    ON("On"),
    ISSUES("Issues")
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) {
                    ElyraTheme.colors.primary
                } else {
                    ElyraTheme.colors.surface
                }
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    ElyraTheme.colors.primary
                } else {
                    ElyraTheme.colors.borderSubtle
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(
            text = label,
            style = ElyraTheme.typography.labelMedium,
            color = if (selected) {
                ElyraTheme.colors.onPrimary
            } else {
                ElyraTheme.colors.textSecondary
            }
        )
    }
}

@Composable
private fun DeviceRow(
    device: Device,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit
) {

    val isOn = device.status == DeviceStatus.ON

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
            .padding(horizontal = 14.dp, vertical = 14.dp),
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
                imageVector = device.type.icon(),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isOn) {
                    ElyraTheme.colors.textPrimary
                } else {
                    ElyraTheme.colors.textTertiary
                }
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.bodyLarge,
                color = ElyraTheme.colors.textPrimary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                StatusDot(status = device.status)

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = device.statusLine(),
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary,
                    maxLines = 1
                )
            }
        }

        Switch(
            checked = isOn,
            onCheckedChange = onToggle,
            enabled = device.isControllable,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ElyraTheme.colors.onPrimary,
                checkedTrackColor = ElyraTheme.colors.primary,
                uncheckedThumbColor = ElyraTheme.colors.textTertiary,
                uncheckedTrackColor = ElyraTheme.colors.surfaceInteractive
            )
        )
    }
}

@Composable
private fun StatusDot(
    status: DeviceStatus
) {

    val color = when (status) {
        DeviceStatus.ON -> ElyraTheme.colors.success
        DeviceStatus.OFF -> ElyraTheme.colors.textTertiary
        DeviceStatus.ERROR -> ElyraTheme.colors.error
        DeviceStatus.DISCONNECTED -> ElyraTheme.colors.warning
    }

    Box(
        modifier = Modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
private fun EmptyDevices(
    onAddDevice: () -> Unit
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
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No devices yet",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Add lights, outlets, switches, appliances or cameras " +
                "to start controlling your home.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(ElyraTheme.colors.primary)
                .clickable(onClick = onAddDevice)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {

            Text(
                text = "Add device",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.onPrimary
            )
        }
    }
}

private fun Device.statusLine(): String {

    val typeLabel = type.label()

    return when (status) {
        DeviceStatus.ON ->
            if (type == DeviceType.MULTI_SWITCH) {
                "$activeChannelCount of ${switches.size} on · $typeLabel"
            } else {
                "On · $typeLabel"
            }
        DeviceStatus.OFF -> "Off · $typeLabel"
        DeviceStatus.ERROR -> "Error reported · $typeLabel"
        DeviceStatus.DISCONNECTED -> "Disconnected · $typeLabel"
    }
}

private fun DeviceType.icon(): ImageVector =
    when (this) {
        DeviceType.LIGHT -> Icons.Outlined.Lightbulb
        DeviceType.OUTLET -> Icons.Outlined.Power
        DeviceType.MULTI_SWITCH -> Icons.Outlined.Tune
        DeviceType.SAFETY_APPLIANCE -> Icons.Outlined.Security
        DeviceType.SECURITY_CAMERA -> Icons.Outlined.CameraAlt
    }

private fun DeviceType.label(): String =
    when (this) {
        DeviceType.LIGHT -> "Light"
        DeviceType.OUTLET -> "Outlet"
        DeviceType.MULTI_SWITCH -> "Multi-switch"
        DeviceType.SAFETY_APPLIANCE -> "Safety appliance"
        DeviceType.SECURITY_CAMERA -> "Camera"
    }
