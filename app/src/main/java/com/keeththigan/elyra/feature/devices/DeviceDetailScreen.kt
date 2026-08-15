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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel

// ============================================================================
// DEVICE DETAIL SCREEN
// ============================================================================

@Composable
fun DeviceDetailScreen(
    deviceId: String,
    deviceViewModel: DeviceViewModel,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel,
    onEditDevice: () -> Unit,
    onRemoveDevice: () -> Unit,
    onBack: () -> Unit
) {

    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()
    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(deviceId) {
        deviceViewModel.loadDevice(deviceId)
    }

    val device = deviceState.selectedDevice

    LaunchedEffect(device?.floorId, device?.roomId) {
        device?.floorId?.takeIf { it.isNotBlank() }?.let {
            floorViewModel.loadFloor(it)
        }
        device?.roomId?.takeIf { it.isNotBlank() }?.let {
            roomViewModel.loadRoom(it)
        }
    }

    LaunchedEffect(deviceState.isDeleted) {
        if (deviceState.isDeleted) {
            deviceViewModel.consumeDeleted()
            onBack()
        }
    }

    if (device == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    ElyraTheme.colors.background
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (deviceState.error != null) {
                    deviceState.error ?: ""
                } else {
                    "Loading device…"
                },
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )
        }

        return
    }

    val floorName = floorState.selectedFloor?.name ?: ""
    val roomName = roomState.selectedRoom?.name ?: ""

    val isOn = device.status == DeviceStatus.ON

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
            horizontal = 16.dp,
            vertical = 10.dp
        ),
    verticalAlignment = Alignment.CenterVertically
) {

    IconButton(
        onClick = onBack
    ) {

        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = "Back",
            tint = ElyraTheme.colors.textPrimary
        )
    }

    Spacer(
        modifier = Modifier.size(8.dp)
    )

    Column(
        modifier = Modifier.weight(1f)
    ) {

        Text(
            text = device.name,
            style = ElyraTheme.typography.titleLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Text(
            text = listOf(floorName, roomName)
                .filter { it.isNotBlank() }
                .joinToString(" · "),
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }

    // EDIT DEVICE
    IconButton(
        onClick = onEditDevice
    ) {

        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit device",
            modifier = Modifier.size(21.dp),
            tint = ElyraTheme.colors.textSecondary
        )
    }
}

        // ====================================================================
        // CONTENT
        // ====================================================================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(14.dp)
            )


            // =================================================================
            // DEVICE HEADER
            // =================================================================

            DeviceHeader(
                device = device,
                floorName = floorName,
                roomName = roomName,
                isOn = isOn
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // =================================================================
            // POWER CONTROL
            // =================================================================

            PowerControlCard(
                isOn = isOn,
                onToggle = {
                    deviceViewModel.toggleDevice(deviceId, it)
                }
            )


            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // =================================================================
            // DEVICE-SPECIFIC CONTROLS
            // =================================================================

            when (device.type) {

                DeviceType.LIGHT -> {

                    LightControls()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    ScheduleCard()
                }

                DeviceType.OUTLET -> {

                    UsageCard()
                }

                DeviceType.MULTI_SWITCH -> {

                    MultiSwitchControls()
                }

                DeviceType.SAFETY_APPLIANCE -> {

                    SafetyControls(
                        isOn = isOn
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    UsageCard()
                }

                DeviceType.SECURITY_CAMERA -> {

                    CameraPreview()

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    CameraStatus()
                }
            }


            Spacer(
    modifier = Modifier.height(28.dp)
)

RemoveDeviceButton(
    onClick = onRemoveDevice
)

Spacer(
    modifier = Modifier.height(30.dp)
)
        }
    }
}


// ============================================================================
// DEVICE HEADER
// ============================================================================

@Composable
private fun DeviceHeader(
    device: Device,
    floorName: String,
    roomName: String,
    isOn: Boolean
) {

    val activeColor = Color(0xFF22C55E)

    val statusColor =
        if (isOn) {
            activeColor
        } else {
            ElyraTheme.colors.textTertiary
        }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .size(64.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(
                    statusColor.copy(
                        alpha = 0.10f
                    )
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = deviceIcon(device.type),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = statusColor
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = device.name,
            style = ElyraTheme.typography.headlineMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = listOf(floorName, roomName)
                .filter { it.isNotBlank() }
                .joinToString(" · "),
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// POWER CONTROL
// ============================================================================

@Composable
private fun PowerControlCard(
    isOn: Boolean,
    onToggle: (Boolean) -> Unit
) {

    val green = Color(0xFF22C55E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    if (isOn) {
                        green.copy(alpha = 0.12f)
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Power,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint =
                    if (isOn) {
                        green
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Power",
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = if (isOn) "Currently on" else "Currently off",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Switch(
            checked = isOn,
            onCheckedChange = onToggle
        )
    }
}


// ============================================================================
// LIGHT CONTROLS
// ============================================================================

@Composable
private fun LightControls() {

    var brightness by remember {
        mutableFloatStateOf(0.75f)
    }

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.Lightbulb,
            title = "Brightness"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "${(brightness * 100).toInt()}%",
            style = ElyraTheme.typography.headlineSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Slider(
            value = brightness,
            onValueChange = {
                brightness = it
            }
        )
    }
}


// ============================================================================
// SCHEDULE
// ============================================================================

@Composable
private fun ScheduleCard() {

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.Schedule,
            title = "Schedule"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Every day",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "07:00 → 23:00",
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// MULTI SWITCH
// ============================================================================

@Composable
private fun MultiSwitchControls() {

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.Power,
            title = "Switches"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SwitchRow(
            name = "Main Light",
            initialValue = true
        )

        SwitchRow(
            name = "Counter Light",
            initialValue = false
        )

        SwitchRow(
            name = "Dining Light",
            initialValue = true
        )
    }
}


// ============================================================================
// SWITCH ROW
// ============================================================================

@Composable
private fun SwitchRow(
    name: String,
    initialValue: Boolean
) {

    var checked by remember {
        mutableStateOf(initialValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = name,
            modifier = Modifier.weight(1f),
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}


// ============================================================================
// SAFETY CONTROLS
// ============================================================================

@Composable
private fun SafetyControls(
    isOn: Boolean
) {

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.Security,
            title = "Safety"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Column {

                Text(
                    text = "Maximum ON duration",
                    style = ElyraTheme.typography.titleSmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "30 minutes",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFF22C55E)
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = if (isOn) {
                    "Safety timer is running"
                } else {
                    "Safety cutoff enabled"
                },
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}


// ============================================================================
// CAMERA
// ============================================================================

@Composable
private fun CameraPreview() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Camera snapshot",
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Mock camera stream",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// CAMERA STATUS
// ============================================================================

@Composable
private fun CameraStatus() {

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.CameraAlt,
            title = "Camera status"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Connected",
            style = ElyraTheme.typography.titleSmall,
            color = Color(0xFF22C55E)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Last updated just now",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}




// ============================================================================
// USAGE
// ============================================================================

@Composable
private fun UsageCard() {

    SettingsCard {

        CardTitle(
            icon = Icons.Outlined.Thermostat,
            title = "Usage"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            UsageItem(
                title = "Today",
                value = "2h 14m"
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            UsageItem(
                title = "This week",
                value = "12h 42m"
            )
        }
    }
}


// ============================================================================
// USAGE ITEM
// ============================================================================

@Composable
private fun UsageItem(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier.width(90.dp)
    ) {

        Text(
            text = title,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = value,
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )
    }
}


// ============================================================================
// GENERIC SETTINGS CARD
// ============================================================================

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(18.dp)
    ) {

        content()
    }
}


// ============================================================================
// CARD TITLE
// ============================================================================

@Composable
private fun CardTitle(
    icon: ImageVector,
    title: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )

        Text(
            text = title,
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )
    }
}


// ============================================================================
// DEVICE ICON
// ============================================================================

private fun deviceIcon(
    type: DeviceType
): ImageVector {

    return when (type) {

        DeviceType.LIGHT ->
            Icons.Outlined.Lightbulb

        DeviceType.OUTLET ->
            Icons.Outlined.Power

        DeviceType.MULTI_SWITCH ->
            Icons.Outlined.Power

        DeviceType.SAFETY_APPLIANCE ->
            Icons.Outlined.Thermostat

        DeviceType.SECURITY_CAMERA ->
            Icons.Outlined.CameraAlt
    }
}
// ============================================================================
// REMOVE DEVICE
// ============================================================================

@Composable
private fun RemoveDeviceButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    ElyraTheme.colors.error.copy(
                        alpha = 0.10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = ElyraTheme.colors.error
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Remove device",
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.error
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "Remove this device from your system",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}