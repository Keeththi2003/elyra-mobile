package com.keeththigan.elyra.feature.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.Timestamp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.topbar.ElyraDetailTopBar
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceConnectivity
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel
import kotlinx.coroutines.delay

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
                .background(ElyraTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = deviceState.error ?: "Loading device…",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )
        }

        return
    }

    val floorName =
        floorState.floors.find { it.id == device.floorId }?.name.orEmpty()

    val roomName =
        roomState.rooms.find { it.id == device.roomId }?.name.orEmpty()

    val location =
        listOf(floorName, roomName)
            .filter { it.isNotBlank() }
            .joinToString(" · ")
            .ifBlank { "Unassigned" }

    val isOn = device.status == DeviceStatus.ON

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        // ====================================================================
        // TOP BAR
        // ====================================================================

        ElyraDetailTopBar(
            title = device.name,
            subtitle = location,
            onBack = onBack,
            actions = {
                IconButton(onClick = onEditDevice) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit device",
                        modifier = Modifier.size(20.dp),
                        tint = ElyraTheme.colors.textSecondary
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            // ================================================================
            // HERO
            // ================================================================

            DeviceHero(
                device = device,
                location = location,
                isOn = isOn
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ================================================================
            // POWER
            // ================================================================

            PowerCard(
                isOn = isOn,
                statusLabel = device.status.label(),
                enabled = device.isControllable,
                onToggle = { deviceViewModel.toggleDevice(deviceId, it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ================================================================
            // TYPE-SPECIFIC CONTROLS
            // ================================================================

            when (device.type) {

                DeviceType.LIGHT -> {

                    BrightnessCard(
                        device = device,
                        onBrightnessChange = {
                            deviceViewModel.setBrightness(deviceId, it)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    ScheduleCard(
                        device = device,
                        onScheduleChange = { enabled, start, end ->
                            deviceViewModel.setSchedule(
                                deviceId, enabled, start, end
                            )
                        }
                    )
                }

                DeviceType.OUTLET -> {

                    UsageCard(device = device)
                }

                DeviceType.MULTI_SWITCH -> {

                    MultiSwitchCard(
                        device = device,
                        onChannelToggle = { index, on ->
                            deviceViewModel.toggleSwitchChannel(
                                deviceId, index, on
                            )
                        },
                        onChannelRename = { index, newName ->
                            deviceViewModel.renameSwitchChannel(
                                deviceId, index, newName
                            )
                        }
                    )
                }

                DeviceType.SAFETY_APPLIANCE -> {

                    SafetyCard(
                        device = device,
                        isOn = isOn,
                        onMaxDurationChange = {
                            deviceViewModel.setMaxOnDuration(deviceId, it)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    UsageCard(device = device)
                }

                DeviceType.SECURITY_CAMERA -> {

                    CameraCard(device = device)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ================================================================
            // CONNECTIVITY
            // ================================================================

            ConnectivityCard(device = device)

            Spacer(modifier = Modifier.height(28.dp))

            RemoveDeviceButton(onClick = onRemoveDevice)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


// ============================================================================
// HERO
// ============================================================================

@Composable
private fun DeviceHero(
    device: Device,
    location: String,
    isOn: Boolean
) {

    val accent =
        if (isOn) {
            ElyraTheme.colors.textPrimary
        } else {
            ElyraTheme.colors.textTertiary
        }

    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = device.type.icon(),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = accent
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = device.name,
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = device.type.label(),
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Text(
                text = "  ·  ",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textTertiary
            )

            Text(
                text = location,
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// POWER
// ============================================================================

@Composable
private fun PowerCard(
    isOn: Boolean,
    statusLabel: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {

    ElyraCard {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Power",
                    style = ElyraTheme.typography.titleMedium,
                    color = if (enabled) {
                        ElyraTheme.colors.textPrimary
                    } else {
                        ElyraTheme.colors.textDisabled
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (enabled) {
                        statusLabel
                    } else {
                        "Unavailable while the device is unreachable"
                    },
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            Switch(
                checked = isOn,
                onCheckedChange = onToggle,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElyraTheme.colors.onPrimary,
                    checkedTrackColor = ElyraTheme.colors.primary,
                    uncheckedThumbColor = ElyraTheme.colors.textTertiary,
                    uncheckedTrackColor = ElyraTheme.colors.surfaceSecondary
                )
            )
        }
    }
}


// ============================================================================
// LIGHT — BRIGHTNESS
// ============================================================================

@Composable
private fun BrightnessCard(
    device: Device,
    onBrightnessChange: (Int) -> Unit
) {

    var sliderValue by remember(device.id) {
        mutableFloatStateOf((device.brightness ?: 80).toFloat())
    }

    ElyraCard {

        CardHeader(
            icon = Icons.Outlined.Lightbulb,
            title = "Brightness"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${sliderValue.toInt()}%",
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                onBrightnessChange(sliderValue.toInt())
            },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = ElyraTheme.colors.primary,
                activeTrackColor = ElyraTheme.colors.primary,
                inactiveTrackColor = ElyraTheme.colors.surfaceInteractive
            )
        )
    }
}


// ============================================================================
// SCHEDULE
// ============================================================================

@Composable
private fun ScheduleCard(
    device: Device,
    onScheduleChange: (Boolean, String?, String?) -> Unit
) {

    var enabled by remember(device.id) {
        mutableStateOf(device.scheduleEnabled)
    }

    var start by remember(device.id) {
        mutableStateOf(device.scheduleStart ?: "07:00")
    }

    var end by remember(device.id) {
        mutableStateOf(device.scheduleEnd ?: "23:00")
    }

    ElyraCard {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier.weight(1f)) {

                CardHeader(
                    icon = Icons.Outlined.Schedule,
                    title = "Schedule"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (enabled) {
                        "Runs automatically every day"
                    } else {
                        "Automatic run is off"
                    },
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = it
                    onScheduleChange(it, start, end)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ElyraTheme.colors.onPrimary,
                    checkedTrackColor = ElyraTheme.colors.primary,
                    uncheckedThumbColor = ElyraTheme.colors.textTertiary,
                    uncheckedTrackColor = ElyraTheme.colors.surfaceSecondary
                )
            )
        }

        if (enabled) {

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                TimeField(
                    label = "Starts",
                    value = start,
                    onValueChange = {
                        start = it
                        onScheduleChange(enabled, it, end)
                    },
                    modifier = Modifier.weight(1f)
                )

                TimeField(
                    label = "Ends",
                    value = end,
                    onValueChange = {
                        end = it
                        onScheduleChange(enabled, start, it)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
private fun TimeField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        Text(
            text = label,
            style = ElyraTheme.typography.labelMedium,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            value = value,
            onValueChange = {
                if (it.length <= 5) onValueChange(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = ElyraTheme.typography.titleMedium.copy(
                color = ElyraTheme.colors.textPrimary
            ),
            decorationBox = { inner ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElyraTheme.colors.surfaceSecondary)
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    inner()
                }
            }
        )
    }
}


// ============================================================================
// MULTI-SWITCH — individually addressable channels
// ============================================================================

@Composable
private fun MultiSwitchCard(
    device: Device,
    onChannelToggle: (Int, Boolean) -> Unit,
    onChannelRename: (Int, String) -> Unit
) {

    var renamingIndex by remember(device.id) {
        mutableStateOf<Int?>(null)
    }

    ElyraCard {

        CardHeader(
            icon = Icons.Outlined.Tune,
            title = "Switches"
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${device.switches.size}-gang unit · " +
                "${device.activeChannelCount} on · each switch is addressed " +
                "independently",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        device.switches.sortedBy { it.index }.forEach { channel ->

            if (renamingIndex == channel.index) {

                ChannelRenameRow(
                    initialName = channel.displayName(),
                    onDone = { newName ->
                        onChannelRename(channel.index, newName)
                        renamingIndex = null
                    },
                    onCancel = { renamingIndex = null }
                )

            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (channel.isOn) {
                                    ElyraTheme.colors.textPrimary
                                } else {
                                    ElyraTheme.colors.borderStrong
                                }
                            )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { renamingIndex = channel.index }
                    ) {

                        Text(
                            text = channel.displayName(),
                            style = ElyraTheme.typography.bodyLarge,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Text(
                            text = if (channel.isOn) "On · tap to rename"
                            else "Off · tap to rename",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )
                    }

                    Switch(
                        checked = channel.isOn,
                        onCheckedChange = {
                            onChannelToggle(channel.index, it)
                        },
                        enabled = device.isControllable,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ElyraTheme.colors.onPrimary,
                            checkedTrackColor = ElyraTheme.colors.primary,
                            uncheckedThumbColor = ElyraTheme.colors.textTertiary,
                            uncheckedTrackColor = ElyraTheme.colors.surfaceSecondary
                        )
                    )
                }
            }
        }
    }
}


@Composable
private fun ChannelRenameRow(
    initialName: String,
    onDone: (String) -> Unit,
    onCancel: () -> Unit
) {

    var text by remember { mutableStateOf(initialName) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            modifier = Modifier.weight(1f),
            textStyle = ElyraTheme.typography.bodyLarge.copy(
                color = ElyraTheme.colors.textPrimary
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ElyraTheme.colors.surfaceSecondary)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) { inner() }
            }
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Save",
            modifier = Modifier.clickable { onDone(text.trim()) },
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Cancel",
            modifier = Modifier.clickable { onCancel() },
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// SAFETY APPLIANCE — max ON duration + live countdown
// ============================================================================

@Composable
private fun SafetyCard(
    device: Device,
    isOn: Boolean,
    onMaxDurationChange: (Int) -> Unit
) {

    val maxMinutes = device.maxOnDurationMinutes ?: 30

    // Ticks once a second so the remaining-time readout stays live.
    var nowSeconds by remember { mutableLongStateOf(Timestamp.now().seconds) }

    LaunchedEffect(isOn, device.lastOnAt) {
        while (isOn) {
            nowSeconds = Timestamp.now().seconds
            delay(1_000)
        }
    }

    val elapsedSeconds =
        device.lastOnAt?.let { (nowSeconds - it.seconds).coerceAtLeast(0L) } ?: 0L

    val remainingSeconds =
        (maxMinutes * 60L - elapsedSeconds).coerceAtLeast(0L)

    ElyraCard {

        CardHeader(
            icon = Icons.Outlined.Security,
            title = "Safety cutoff"
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "This appliance switches itself off after the maximum " +
                "active duration.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ElyraTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Maximum ON duration",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "$maxMinutes minutes",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            DurationStepper(
                minutes = maxMinutes,
                onChange = onMaxDurationChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ElyraTheme.colors.surfaceSecondary)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ElyraTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = if (isOn) {
                    "Auto-off in ${formatDuration(remainingSeconds)}"
                } else {
                    "Safety cutoff armed"
                },
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}


@Composable
private fun DurationStepper(
    minutes: Int,
    onChange: (Int) -> Unit
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        StepperButton(label = "−") {
            onChange((minutes - 5).coerceAtLeast(1))
        }

        Spacer(modifier = Modifier.width(8.dp))

        StepperButton(label = "+") {
            onChange(minutes + 5)
        }
    }
}


@Composable
private fun StepperButton(
    label: String,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ElyraTheme.colors.surfaceSecondary)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = label,
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )
    }
}


// ============================================================================
// CAMERA
// ============================================================================

@Composable
private fun CameraCard(
    device: Device
) {

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = ElyraTheme.colors.textSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Live snapshot",
                    style = ElyraTheme.typography.titleSmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = device.cameraUri?.takeIf { it.isNotBlank() }
                        ?: "No stream configured",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        ElyraCard {

            CardHeader(
                icon = Icons.Outlined.CameraAlt,
                title = "Stream"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = device.cameraUri?.takeIf { it.isNotBlank() }
                    ?: "Add a stream URI from the edit screen.",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (device.status == DeviceStatus.ON) {
                    "Connected"
                } else {
                    "Offline"
                },
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// USAGE REPORTING
// ============================================================================

@Composable
private fun UsageCard(
    device: Device
) {

    var nowSeconds by remember { mutableLongStateOf(Timestamp.now().seconds) }

    val isOn = device.status == DeviceStatus.ON

    LaunchedEffect(isOn, device.lastOnAt) {
        while (isOn) {
            nowSeconds = Timestamp.now().seconds
            delay(1_000)
        }
    }

    val currentSessionSeconds =
        if (isOn) {
            device.lastOnAt?.let {
                (nowSeconds - it.seconds).coerceAtLeast(0L)
            } ?: 0L
        } else {
            0L
        }

    ElyraCard {

        CardHeader(
            icon = Icons.Outlined.Power,
            title = "Usage"
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {

            UsageStat(
                label = "Current session",
                value = if (isOn) {
                    formatDuration(currentSessionSeconds)
                } else {
                    "—"
                },
                modifier = Modifier.weight(1f)
            )

            UsageStat(
                label = "Total runtime",
                value = formatDuration(
                    device.totalOnSeconds + currentSessionSeconds
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
private fun UsageStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier) {

        Text(
            text = value,
            style = ElyraTheme.typography.titleLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// STATUS
// ============================================================================

@Composable
private fun ConnectivityCard(
    device: Device
) {

    val dotColor = when (device.connectivity) {
        DeviceConnectivity.ONLINE -> ElyraTheme.colors.success
        DeviceConnectivity.OFFLINE -> ElyraTheme.colors.textTertiary
        DeviceConnectivity.ERROR -> ElyraTheme.colors.error
    }

    ElyraCard {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Connectivity",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when (device.connectivity) {
                        DeviceConnectivity.ONLINE ->
                            "Online · reporting normally"
                        DeviceConnectivity.OFFLINE ->
                            "Offline · last known state shown"
                        DeviceConnectivity.ERROR ->
                            "Fault reported by the device"
                    },
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            Text(
                text = device.connectivity.label(),
                style = ElyraTheme.typography.labelMedium,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// SHARED PIECES
// ============================================================================

@Composable
private fun ElyraCard(
    content: @Composable () -> Unit
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
            .padding(20.dp)
    ) {
        content()
    }
}


@Composable
private fun CardHeader(
    icon: ImageVector,
    title: String
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )
    }
}


@Composable
private fun RemoveDeviceButton(
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.DeleteOutline,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.error
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Remove device",
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.error
        )
    }
}


// ============================================================================
// FORMATTING
// ============================================================================

private fun formatDuration(
    seconds: Long
): String {

    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${secs}s"
        else -> "${secs}s"
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
        DeviceType.OUTLET -> "Electrical outlet"
        DeviceType.MULTI_SWITCH -> "Multi-switch"
        DeviceType.SAFETY_APPLIANCE -> "Safety appliance"
        DeviceType.SECURITY_CAMERA -> "Security camera"
    }


private fun DeviceStatus.label(): String =
    when (this) {
        DeviceStatus.ON -> "Currently on"
        DeviceStatus.OFF -> "Currently off"
        DeviceStatus.ERROR -> "Reporting an error"
        DeviceStatus.DISCONNECTED -> "Disconnected"
    }


private fun DeviceConnectivity.label(): String =
    when (this) {
        DeviceConnectivity.ONLINE -> "Online"
        DeviceConnectivity.OFFLINE -> "Offline"
        DeviceConnectivity.ERROR -> "Error"
    }
