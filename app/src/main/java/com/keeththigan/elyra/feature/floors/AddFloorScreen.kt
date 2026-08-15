package com.keeththigan.elyra.feature.floors

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
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
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.devices.DeviceViewModel

// ============================================================================
// TEMPORARY ROOM MODEL
// ============================================================================

private data class FloorRoom(
    val id: Int,
    val name: String,
    val devices: MutableList<Device>
)

// ============================================================================
// ADD FLOOR SCREEN
// ============================================================================

@Composable
fun AddFloorScreen(
    floorViewModel: FloorViewModel,
    deviceViewModel: DeviceViewModel,
    onBack: () -> Unit,
    onAddDevice: () -> Unit,
    onCreateFloor: () -> Unit
) {

    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        deviceViewModel.loadDevices()
    }

    LaunchedEffect(floorState.isSaved) {
        if (floorState.isSaved) {
            onCreateFloor()
            floorViewModel.consumeSaved()
        }
    }

    var floorName by remember {
        mutableStateOf("")
    }

    var roomName by remember {
        mutableStateOf("")
    }

    val rooms = remember {
        mutableStateListOf<FloorRoom>()
    }

    // Devices that already exist but aren't assigned to a floor/room yet.
    val availableDevices =
        deviceState.devices.filter {
            it.floorId.isBlank()
        }

    val canCreate =
        floorName.trim().isNotEmpty() &&
                rooms.isNotEmpty()

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
                modifier = Modifier.width(12.dp)
            )

            Column {

                Text(
                    text = "Create Floor",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Organize rooms and devices",
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // =================================================================
            // FLOOR INFORMATION
            // =================================================================

            item {

                SectionHeader(
                    title = "Floor information",
                    subtitle = "Give your floor a recognizable name"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                FloorTextField(
                    value = floorName,
                    onValueChange = {
                        floorName = it
                    },
                    placeholder = "e.g. Ground Floor"
                )
            }

            // =================================================================
            // ROOMS
            // =================================================================

            item {

                SectionHeader(
                    title = "Rooms",
                    subtitle = "Add the rooms that belong to this floor"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (rooms.isEmpty()) {

                    EmptyRoomsCard()

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }

                rooms.forEach { room ->

                    RoomSetupCard(
                        room = room,
                        availableDevices = availableDevices,
                        onDeleteRoom = {
                            rooms.remove(room)
                        },
                        onAddDevice = onAddDevice
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }

                // -------------------------------------------------------------
                // ADD ROOM
                // -------------------------------------------------------------

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            ElyraTheme.colors.surface
                        )
                        .border(
                            width = 1.dp,
                            color = ElyraTheme.colors.border,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FloorTextField(
                        value = roomName,
                        onValueChange = {
                            roomName = it
                        },
                        placeholder = "Room name",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(
                                RoundedCornerShape(14.dp)
                            )
                            .background(
                                if (roomName.isNotBlank()) {
                                    ElyraTheme.colors.primary
                                } else {
                                    ElyraTheme.colors.surfaceInteractive
                                }
                            )
                            .clickable(
                                enabled = roomName.isNotBlank()
                            ) {

                                val cleanName =
                                    roomName.trim()

                                if (
                                    rooms.none {
                                        it.name.equals(
                                            cleanName,
                                            ignoreCase = true
                                        )
                                    }
                                ) {

                                    rooms.add(
                                        FloorRoom(
                                            id = rooms.size + 1,
                                            name = cleanName,
                                            devices = mutableListOf()
                                        )
                                    )

                                    roomName = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add room",
                            tint =
                                if (roomName.isNotBlank()) {
                                    ElyraTheme.colors.onPrimary
                                } else {
                                    ElyraTheme.colors.textDisabled
                                }
                        )
                    }
                }
            }
        }

        // ====================================================================
        // CREATE FLOOR
        // ====================================================================

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

            if (floorState.error != null) {

                Text(
                    text = floorState.error ?: "",
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
                        if (canCreate && !floorState.isLoading) {
                            ElyraTheme.colors.primary
                        } else {
                            ElyraTheme.colors.surfaceInteractive
                        }
                    )
                    .clickable(
                        enabled = canCreate && !floorState.isLoading
                    ) {
                        floorViewModel.createFloorWithRooms(
                            floorName = floorName.trim(),
                            rooms = rooms.map { room ->
                                room.name to room.devices.map { it.id }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = if (floorState.isLoading) {
                        "Creating floor…"
                    } else {
                        "Create Floor"
                    },
                    style = ElyraTheme.typography.labelLarge,
                    color =
                        if (canCreate && !floorState.isLoading) {
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
// ROOM SETUP CARD
// ============================================================================

@Composable
private fun RoomSetupCard(
    room: FloorRoom,
    availableDevices: List<Device>,
    onDeleteRoom: () -> Unit,
    onAddDevice: () -> Unit
) {

    var showDeviceSelector by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(16.dp)
    ) {

        // --------------------------------------------------------------------
        // ROOM HEADER
        // --------------------------------------------------------------------

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

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
                    imageVector = Icons.Outlined.Apartment,
                    contentDescription = null,
                    modifier = Modifier.size(21.dp),
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
                    text = room.name,
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "${room.devices.size} devices assigned",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            IconButton(
                onClick = onDeleteRoom
            ) {

                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "Delete room",
                    tint = ElyraTheme.colors.textSecondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // --------------------------------------------------------------------
        // SELECT DEVICE
        // --------------------------------------------------------------------

        Text(
            text = "Devices",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (room.devices.isEmpty()) {

            Text(
                text = "No devices assigned yet",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textTertiary
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        // --------------------------------------------------------------------
        // SELECTED DEVICES
        // --------------------------------------------------------------------

        room.devices.forEach { device ->

            SelectedDeviceRow(
                device = device,
                onRemove = {
                    room.devices.remove(device)
                }
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )
        }

        // --------------------------------------------------------------------
        // DROPDOWN
        // --------------------------------------------------------------------

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
                    .clickable {
                        showDeviceSelector =
                            !showDeviceSelector
                    }
                    .padding(
                        horizontal = 14.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Text(
                    text = "Select existing device",
                    style = ElyraTheme.typography.labelLarge,
                    color = ElyraTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = ElyraTheme.colors.textSecondary
                )
            }
        }

        // --------------------------------------------------------------------
        // DEVICE SELECTOR
        // --------------------------------------------------------------------

        if (showDeviceSelector) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
                    .padding(6.dp)
            ) {

                availableDevices
                    .filter { device ->
                        room.devices.none {
                            it.id == device.id
                        }
                    }
                    .forEach { device ->

                        DeviceSelectorRow(
                            device = device,
                            onClick = {

                                room.devices.add(
                                    device
                                )

                                showDeviceSelector = false
                            }
                        )
                    }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                // -------------------------------------------------------------
                // ADD NEW DEVICE
                // -------------------------------------------------------------

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            onAddDevice()
                        }
                        .padding(
                            horizontal = 10.dp,
                            vertical = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                ElyraTheme.colors.primary
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = ElyraTheme.colors.onPrimary
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    Column {

                        Text(
                            text = "Add new device",
                            style = ElyraTheme.typography.labelLarge,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Text(
                            text = "Device not in the list?",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )
                    }
                }
            }
        }
    }
}


// ============================================================================
// SELECTED DEVICE
// ============================================================================

@Composable
private fun SelectedDeviceRow(
    device: Device,
    onRemove: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            )
            .padding(
                horizontal = 10.dp,
                vertical = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        DeviceIcon(
            type = device.type
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = device.type.displayName(),
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(34.dp)
        ) {

            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "Remove device",
                modifier = Modifier.size(18.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// DEVICE SELECTOR ROW
// ============================================================================

@Composable
private fun DeviceSelectorRow(
    device: Device,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 10.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        DeviceIcon(
            type = device.type
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = device.type.displayName(),
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.Check,
            contentDescription = "Select",
            modifier = Modifier.size(18.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// DEVICE ICON
// ============================================================================

@Composable
private fun DeviceIcon(
    type: DeviceType
) {

    val icon: ImageVector

    when (type) {

        DeviceType.LIGHT -> {
            icon = Icons.Outlined.Lightbulb
        }

        DeviceType.OUTLET -> {
            icon = Icons.Outlined.Power
        }

        DeviceType.MULTI_SWITCH -> {
            icon = Icons.Outlined.Tune
        }

        DeviceType.SAFETY_APPLIANCE -> {
            icon = Icons.Outlined.Power
        }

        DeviceType.SECURITY_CAMERA -> {
            icon = Icons.Outlined.Power
        }
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(
                ElyraTheme.colors.surface
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ElyraTheme.colors.textPrimary
        )
    }
}


// ============================================================================
// EMPTY ROOMS
// ============================================================================

@Composable
private fun EmptyRoomsCard() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.Apartment,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "No rooms yet",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = "Add your first room below.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// SECTION HEADER
// ============================================================================

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String
) {

    Column {

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
}


// ============================================================================
// TEXT FIELD
// ============================================================================

@Composable
private fun FloorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
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
                        horizontal = 15.dp
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


// ============================================================================
// DEVICE TYPE NAME
// ============================================================================

private fun DeviceType.displayName(): String {

    return when (this) {

        DeviceType.LIGHT ->
            "Light"

        DeviceType.OUTLET ->
            "Electrical outlet"

        DeviceType.MULTI_SWITCH ->
            "Multi-switch"

        DeviceType.SAFETY_APPLIANCE ->
            "Safety appliance"

        DeviceType.SECURITY_CAMERA ->
            "Security camera"
    }
}