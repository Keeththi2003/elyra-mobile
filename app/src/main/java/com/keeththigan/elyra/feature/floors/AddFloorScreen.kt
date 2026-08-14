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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme


// ============================================================================
// TEMPORARY DATA MODELS
// Later these will come from repository / database
// ============================================================================

private data class AvailableDevice(
    val id: String,
    val name: String,
    val type: String
)

private data class FloorRoom(
    val id: Int,
    val name: String,
    val devices: List<AvailableDevice>
)


// ============================================================================
// ADD FLOOR SCREEN
// ============================================================================

@Composable
fun AddFloorScreen(
    onBack: () -> Unit,
    onAddDevice: () -> Unit,
    onCreateFloor: () -> Unit
) {

    var floorName by remember {
        mutableStateOf("")
    }

    var newRoomName by remember {
        mutableStateOf("")
    }

    val rooms = remember {
        mutableStateListOf<FloorRoom>()
    }

    /*
     * Temporary device catalog.
     *
     * Later this will come from your database/repository.
     */
    val availableDevices = remember {

        mutableStateListOf(

            AvailableDevice(
                id = "device_001",
                name = "Living Room Light",
                type = "Light"
            ),

            AvailableDevice(
                id = "device_002",
                name = "Bedroom Light",
                type = "Light"
            ),

            AvailableDevice(
                id = "device_003",
                name = "Air Conditioner",
                type = "AC"
            ),

            AvailableDevice(
                id = "device_004",
                name = "Kitchen Outlet",
                type = "Outlet"
            )
        )
    }

    val canCreate =
        floorName.isNotBlank() &&
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
                modifier = Modifier.size(12.dp)
            )

            Column {

                Text(
                    text = "Create Floor",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Set up rooms and devices",
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
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // =================================================================
            // FLOOR NAME
            // =================================================================

            item {

                SectionTitle(
                    title = "Floor information",
                    subtitle = "Give your floor a name"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                FormField(
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

                SectionTitle(
                    title = "Rooms",
                    subtitle = "${rooms.size} rooms added"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if (rooms.isNotEmpty()) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        rooms.forEach { room ->

                            RoomCard(
                                room = room,
                                onDelete = {
                                    rooms.remove(room)
                                }
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }

                FormField(
                    value = newRoomName,
                    onValueChange = {
                        newRoomName = it
                    },
                    placeholder = "Enter room name"
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                AddButton(
                    text = "Add room",
                    enabled = newRoomName.isNotBlank(),
                    onClick = {

                        val name =
                            newRoomName.trim()

                        if (
                            name.isNotEmpty() &&
                            rooms.none {
                                it.name.equals(
                                    name,
                                    ignoreCase = true
                                )
                            }
                        ) {

                            rooms.add(
                                FloorRoom(
                                    id = rooms.size + 1,
                                    name = name,
                                    devices = emptyList()
                                )
                            )

                            newRoomName = ""
                        }
                    }
                )
            }


            // =================================================================
            // DEVICE INFORMATION
            // =================================================================

            item {

                SectionTitle(
                    title = "Devices",
                    subtitle = "Assign existing devices to this floor"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                // -------------------------------------------------------------
                // AVAILABLE DEVICES
                // -------------------------------------------------------------

                if (availableDevices.isNotEmpty()) {

                    availableDevices.forEach { device ->

                        AvailableDeviceCard(
                            device = device,
                            onClick = {
                                // Device assignment will be connected
                                // when repository/data layer is added.
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }


                // -------------------------------------------------------------
                // ADD NEW DEVICE
                // -------------------------------------------------------------

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                AddNewDeviceButton(
                    onClick = onAddDevice
                )
            }


            // =================================================================
            // CREATE FLOOR
            // =================================================================

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            if (canCreate) {
                                ElyraTheme.colors.primary
                            } else {
                                ElyraTheme.colors.surfaceInteractive
                            }
                        )
                        .clickable(
                            enabled = canCreate
                        ) {
                            onCreateFloor()
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Create Floor",
                        style = ElyraTheme.typography.labelLarge,
                        color =
                            if (canCreate) {
                                ElyraTheme.colors.onPrimary
                            } else {
                                ElyraTheme.colors.textDisabled
                            }
                    )
                }

                Spacer(
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}


// ============================================================================
// SECTION TITLE
// ============================================================================

@Composable
private fun SectionTitle(
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
// FORM FIELD
// ============================================================================

@Composable
private fun FormField(
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
                        RoundedCornerShape(16.dp)
                    )
                    .background(
                        ElyraTheme.colors.surface
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


// ============================================================================
// ROOM CARD
// ============================================================================

@Composable
private fun RoomCard(
    room: FloorRoom,
    onDelete: () -> Unit
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
            .padding(
                horizontal = 14.dp,
                vertical = 13.dp
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
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = room.name,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "${room.devices.size} devices",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        IconButton(
            onClick = onDelete
        ) {

            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "Remove room",
                tint = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// AVAILABLE DEVICE CARD
// ============================================================================

@Composable
private fun AvailableDeviceCard(
    device: AvailableDevice,
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
                horizontal = 14.dp,
                vertical = 14.dp
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
                    Color(0xFF22C55E).copy(
                        alpha = 0.10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(21.dp),
                tint = Color(0xFF22C55E)
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
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

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Select device",
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// ADD ROOM BUTTON
// ============================================================================

@Composable
private fun AddButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                if (enabled) {
                    ElyraTheme.colors.surface
                } else {
                    ElyraTheme.colors.surfaceInteractive
                }
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            modifier = Modifier.size(19.dp),
            tint =
                if (enabled) {
                    ElyraTheme.colors.textPrimary
                } else {
                    ElyraTheme.colors.textDisabled
                }
        )

        Spacer(
            modifier = Modifier.size(7.dp)
        )

        Text(
            text = text,
            style = ElyraTheme.typography.labelLarge,
            color =
                if (enabled) {
                    ElyraTheme.colors.textPrimary
                } else {
                    ElyraTheme.colors.textDisabled
                }
        )
    }
}


// ============================================================================
// ADD NEW DEVICE
// ============================================================================

@Composable
private fun AddNewDeviceButton(
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
                horizontal = 16.dp,
                vertical = 14.dp
            ),
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
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Add new device",
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "Register a device not in your device list",
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