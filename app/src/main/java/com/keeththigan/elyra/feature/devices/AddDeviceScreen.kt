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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.keeththigan.elyra.data.model.DeviceType

@Composable
fun AddDeviceScreen(
    deviceViewModel: DeviceViewModel,
    floorId: String = "",
    roomId: String = "",
    onBack: () -> Unit,
    onDeviceCreated: () -> Unit
) {

    val state by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onDeviceCreated()
            deviceViewModel.consumeSaved()
        }
    }

    var deviceName by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf<DeviceType?>(null)
    }

    var startsOn by remember {
        mutableStateOf(false)
    }

    // Per-channel names for a multi-switch gang box, e.g. "Ceiling Fan".
    val switchNames = remember {
        mutableStateListOf("", "", "", "", "", "", "", "")
    }

    var brightness by remember {
        mutableStateOf("80")
    }

    var switchCount by remember {
        mutableStateOf("2")
    }

    var maxOnDuration by remember {
        mutableStateOf("30")
    }

    var cameraUri by remember {
        mutableStateOf("")
    }

    val canCreate =
        deviceName.trim().isNotEmpty() &&
                selectedType != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
    ) {

        // ============================================================
        // TOP BAR
        // ============================================================

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
                    .clip(CircleShape)
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
                modifier = Modifier.width(14.dp)
            )

            Column {

                Text(
                    text = "Add device",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Set up a new device",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        // ============================================================
        // CONTENT
        // ============================================================

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // ========================================================
            // INTRO
            // ========================================================

            Text(
                text = "Let's add your device",
                style = ElyraTheme.typography.headlineSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Give it a name and choose what kind of device you're adding.",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // ========================================================
            // NAME
            // ========================================================

            Text(
                text = "Device name",
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Choose a name that's easy to recognize.",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            AddDeviceTextField(
                value = deviceName,
                onValueChange = {
                    deviceName = it
                },
                placeholder = "e.g. Living Room Light"
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            // ========================================================
            // DEVICE TYPE
            // ========================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Device type",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "*",
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Select the category that best matches your device.",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // ========================================================
            // DEVICE TYPES
            // ========================================================

            DeviceTypeCard(
                type = DeviceType.LIGHT,
                selected = selectedType == DeviceType.LIGHT,
                onClick = {
                    selectedType = DeviceType.LIGHT
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            DeviceTypeCard(
                type = DeviceType.OUTLET,
                selected = selectedType == DeviceType.OUTLET,
                onClick = {
                    selectedType = DeviceType.OUTLET
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            DeviceTypeCard(
                type = DeviceType.MULTI_SWITCH,
                selected = selectedType == DeviceType.MULTI_SWITCH,
                onClick = {
                    selectedType = DeviceType.MULTI_SWITCH
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            DeviceTypeCard(
                type = DeviceType.SAFETY_APPLIANCE,
                selected = selectedType == DeviceType.SAFETY_APPLIANCE,
                onClick = {
                    selectedType = DeviceType.SAFETY_APPLIANCE
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            DeviceTypeCard(
                type = DeviceType.SECURITY_CAMERA,
                selected = selectedType == DeviceType.SECURITY_CAMERA,
                onClick = {
                    selectedType = DeviceType.SECURITY_CAMERA
                }
            )

            // ========================================================
            // TYPE-SPECIFIC SETTINGS
            // ========================================================

            if (selectedType != null) {

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                when (selectedType) {

                    DeviceType.LIGHT -> {

                        Text(
                            text = "Light settings",
                            style = ElyraTheme.typography.titleMedium,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Set the starting brightness.",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        AddDeviceTextField(
                            value = brightness,
                            onValueChange = {
                                brightness = it
                            },
                            placeholder = "Brightness (%)"
                        )
                    }

                    DeviceType.OUTLET -> {

                        DeviceInfoCard(
                            icon = Icons.Outlined.Power,
                            title = "Single outlet",
                            description = "This device will control one electrical outlet."
                        )
                    }

                    DeviceType.MULTI_SWITCH -> {

                        Text(
                            text = "Switch settings",
                            style = ElyraTheme.typography.titleMedium,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "How many switches does this unit contain?",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        AddDeviceTextField(
                            value = switchCount,
                            onValueChange = {
                                switchCount = it
                            },
                            placeholder = "Number of switches"
                        )

                        val channelCount =
                            (switchCount.toIntOrNull() ?: 0).coerceIn(0, 8)

                        if (channelCount > 0) {

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Text(
                                text = "Name each switch",
                                style = ElyraTheme.typography.titleMedium,
                                color = ElyraTheme.colors.textPrimary
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Names make the gang box usable — " +
                                    "\"Ceiling Fan\" beats \"Switch 2\".",
                                style = ElyraTheme.typography.bodySmall,
                                color = ElyraTheme.colors.textSecondary
                            )

                            repeat(channelCount) { index ->

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                AddDeviceTextField(
                                    value = switchNames[index],
                                    onValueChange = {
                                        switchNames[index] = it
                                    },
                                    placeholder = "Switch ${index + 1} name"
                                )
                            }
                        }
                    }

                    DeviceType.SAFETY_APPLIANCE -> {

                        DeviceInfoCard(
                            icon = Icons.Outlined.WarningAmber,
                            title = "Safety protection enabled",
                            description = "This device can automatically switch OFF after the configured maximum duration.",
                            warning = true
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        AddDeviceTextField(
                            value = maxOnDuration,
                            onValueChange = {
                                maxOnDuration = it
                            },
                            placeholder = "Maximum ON time (minutes)"
                        )
                    }

                    DeviceType.SECURITY_CAMERA -> {

                        Text(
                            text = "Camera source",
                            style = ElyraTheme.typography.titleMedium,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Add the stream or snapshot source for this camera.",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        AddDeviceTextField(
                            value = cameraUri,
                            onValueChange = {
                                cameraUri = it
                            },
                            placeholder = "Camera stream URI"
                        )
                    }

                    null -> Unit
                }

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                // ====================================================
                // INITIAL STATE
                // ====================================================

                Text(
                    text = "Initial state",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Choose how the device should appear when added.",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    StateOption(
                        title = "OFF",
                        selected = !startsOn,
                        color = ElyraTheme.colors.textSecondary,
                        modifier = Modifier.weight(1f)
                    ) {
                        startsOn = false
                    }

                    StateOption(
                        title = "ON",
                        selected = startsOn,
                        color = Color(0xFF22C55E),
                        modifier = Modifier.weight(1f)
                    ) {
                        startsOn = true
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        // ============================================================
        // BOTTOM ACTION
        // ============================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    ElyraTheme.colors.background
                )
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                )
        ) {

            if (state.error != null) {

                Text(
                    text = state.error ?: "",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.error
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .background(
                        if (canCreate && !state.isLoading) {
                            ElyraTheme.colors.primary
                        } else {
                            ElyraTheme.colors.surfaceInteractive
                        }
                    )
                    .clickable(
                        enabled = canCreate && !state.isLoading
                    ) {
                        deviceViewModel.createDevice(
                            name = deviceName.trim(),
                            type = selectedType!!,
                            floorId = floorId,
                            roomId = roomId,
                            isOn = startsOn,
                            brightness = brightness.toIntOrNull(),
                            switchCount = switchCount.toIntOrNull(),
                            switchNames = switchNames.toList(),
                            maxOnDurationMinutes = maxOnDuration.toIntOrNull(),
                            cameraUri = cameraUri.ifBlank { null }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = if (state.isLoading) {
                        "Adding device…"
                    } else {
                        "Add device"
                    },
                    style = ElyraTheme.typography.labelLarge,
                    color =
                        if (canCreate && !state.isLoading) {
                            ElyraTheme.colors.onPrimary
                        } else {
                            ElyraTheme.colors.textDisabled
                        }
                )
            }
        }
    }
}


// ============================================================================
// DEVICE TYPE CARD
// ============================================================================

@Composable
private fun DeviceTypeCard(
    type: DeviceType,
    selected: Boolean,
    onClick: () -> Unit
) {

    val icon: ImageVector

    val title: String

    val description: String

    when (type) {

        DeviceType.LIGHT -> {

            icon = Icons.Outlined.Lightbulb

            title = "Light"

            description =
                "Control lighting and brightness"
        }

        DeviceType.OUTLET -> {

            icon = Icons.Outlined.Power

            title = "Electrical outlet"

            description =
                "Control a connected electrical outlet"
        }

        DeviceType.MULTI_SWITCH -> {

            icon = Icons.Outlined.Tune

            title = "Multi-switch"

            description =
                "Control multiple switches independently"
        }

        DeviceType.SAFETY_APPLIANCE -> {

            icon = Icons.Outlined.WarningAmber

            title = "Safety appliance"

            description =
                "Iron or other safety-critical appliance"
        }

        DeviceType.SECURITY_CAMERA -> {

            icon = Icons.Outlined.CameraAlt

            title = "Security camera"

            description =
                "Monitor your home with a camera"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                if (selected) {
                    ElyraTheme.colors.primary
                } else {
                    ElyraTheme.colors.surface
                }
            )
            .then(
                if (!selected) {
                    Modifier.border(
                        width = 1.dp,
                        color = ElyraTheme.colors.border,
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Modifier
                }
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
                .size(46.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(
                    if (selected) {
                        Color.White.copy(alpha = 0.16f)
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(23.dp),
                tint =
                    if (selected) {
                        ElyraTheme.colors.onPrimary
                    } else {
                        ElyraTheme.colors.textPrimary
                    }
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = ElyraTheme.typography.titleSmall,
                color =
                    if (selected) {
                        ElyraTheme.colors.onPrimary
                    } else {
                        ElyraTheme.colors.textPrimary
                    }
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                style = ElyraTheme.typography.bodySmall,
                color =
                    if (selected) {
                        ElyraTheme.colors.onPrimary.copy(
                            alpha = 0.72f
                        )
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        if (selected) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.onPrimary
                    )
            )
        }
    }
}


// ============================================================================
// INFO CARD
// ============================================================================

@Composable
private fun DeviceInfoCard(
    icon: ImageVector,
    title: String,
    description: String,
    warning: Boolean = false
) {

    val color =
        if (warning) {
            Color(0xFFF59E0B)
        } else {
            ElyraTheme.colors.primary
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                color.copy(
                    alpha = 0.08f
                )
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(
                    color.copy(
                        alpha = 0.12f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = color
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = title,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// STATE OPTION
// ============================================================================

@Composable
private fun StateOption(
    title: String,
    selected: Boolean,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (selected) {
                    color.copy(
                        alpha = 0.12f
                    )
                } else {
                    ElyraTheme.colors.surface
                }
            )
            .then(
                if (selected) {
                    Modifier.border(
                        width = 1.dp,
                        color = color.copy(
                            alpha = 0.5f
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = ElyraTheme.colors.border,
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            )
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = title,
            style = ElyraTheme.typography.labelLarge,
            color = color
        )
    }
}


// ============================================================================
// TEXT FIELD
// ============================================================================

@Composable
private fun AddDeviceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        textStyle = ElyraTheme.typography.bodyLarge.copy(
            color = ElyraTheme.colors.textPrimary
        ),
        decorationBox = { innerTextField ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        ElyraTheme.colors.surface
                    )
                    .border(
                        width = 1.dp,
                        color = ElyraTheme.colors.border,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(
                        horizontal = 16.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {

                if (value.isEmpty()) {

                    Text(
                        text = placeholder,
                        style = ElyraTheme.typography.bodyLarge,
                        color = ElyraTheme.colors.textTertiary
                    )
                }

                innerTextField()
            }
        }
    )
}