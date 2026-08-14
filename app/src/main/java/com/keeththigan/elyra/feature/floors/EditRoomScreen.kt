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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.RemoveCircleOutline
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
// TEMPORARY DEVICE MODEL
// Later this will come from the database using roomId
// ============================================================================

private data class RoomDevice(
    val id: String,
    val name: String,
    val type: String,
    val isOn: Boolean
)


// ============================================================================
// EDIT ROOM SCREEN
// ============================================================================

@Composable
fun EditRoomScreen(
    roomId: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {

    // ------------------------------------------------------------
    // Temporary room data
    // Later:
    // roomId -> ViewModel -> Repository -> Database
    // ------------------------------------------------------------

    var roomName by remember {
        mutableStateOf("Living Room")
    }

    // ------------------------------------------------------------
    // Devices currently assigned to this room
    // ------------------------------------------------------------

    val devices = remember {

        mutableStateListOf(

            RoomDevice(
                id = "device_001",
                name = "Living Room Light",
                type = "Light",
                isOn = true
            ),

            RoomDevice(
                id = "device_002",
                name = "Smart Outlet",
                type = "Outlet",
                isOn = false
            ),

            RoomDevice(
                id = "device_003",
                name = "Air Conditioner",
                type = "AC",
                isOn = false
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
        // TOP BAR
        // ================================================================

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
                modifier = Modifier.size(12.dp)
            )

            Column {

                Text(
                    text = "Edit Room",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Manage room information",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }


        // ================================================================
        // CONTENT
        // ================================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // =============================================================
            // ROOM INFORMATION
            // =============================================================

            item {

                SectionTitle(
                    title = "Room information",
                    subtitle = "Change the name of this room"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                RoomNameField(
                    value = roomName,
                    onValueChange = {
                        roomName = it
                    }
                )
            }


            // =============================================================
            // ROOM ID
            // =============================================================

            item {

                SectionTitle(
                    title = "Room",
                    subtitle = "Room information"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            ElyraTheme.colors.surface
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(
                                RoundedCornerShape(14.dp)
                            )
                            .background(
                                ElyraTheme.colors.surfaceSecondary
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.MeetingRoom,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = ElyraTheme.colors.textPrimary
                        )
                    }

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Column {

                        Text(
                            text = "Room ID",
                            style = ElyraTheme.typography.labelMedium,
                            color = ElyraTheme.colors.textSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = roomId,
                            style = ElyraTheme.typography.bodyMedium,
                            color = ElyraTheme.colors.textPrimary
                        )
                    }
                }
            }


            // =============================================================
            // DEVICES
            // =============================================================

            item {

                SectionTitle(
                    title = "Devices",
                    subtitle = "${devices.size} devices assigned to this room"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if (devices.isEmpty()) {

                    EmptyDevicesCard()

                } else {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        devices.forEach { device ->

                            RoomDeviceCard(
                                device = device,
                                onRemove = {
                                    devices.remove(device)
                                }
                            )
                        }
                    }
                }
            }


            // =============================================================
            // SAVE
            // =============================================================

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                SaveButton(
                    enabled = roomName.isNotBlank(),
                    onClick = onSave
                )
            }


            // =============================================================
            // DELETE ROOM
            // =============================================================

            item {

                DeleteRoomButton(
                    onClick = onDelete
                )

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
// ROOM NAME FIELD
// ============================================================================

@Composable
private fun RoomNameField(
    value: String,
    onValueChange: (String) -> Unit
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
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                if (value.isEmpty()) {

                    Text(
                        text = "Room name",
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
// DEVICE CARD
// ============================================================================

@Composable
private fun RoomDeviceCard(
    device: RoomDevice,
    onRemove: () -> Unit
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
            .padding(
                horizontal = 14.dp,
                vertical = 13.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ------------------------------------------------------------
        // ICON
        // ------------------------------------------------------------

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(
                    RoundedCornerShape(13.dp)
                )
                .background(
                    if (device.isOn) {
                        Color(0xFF22C55E).copy(alpha = 0.10f)
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint =
                    if (device.isOn) {
                        Color(0xFF22C55E)
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )


        // ------------------------------------------------------------
        // DEVICE INFORMATION
        // ------------------------------------------------------------

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = device.type,
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )

                Spacer(
                    modifier = Modifier.size(7.dp)
                )

                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(
                            androidx.compose.foundation.shape.CircleShape
                        )
                        .background(
                            if (device.isOn) {
                                Color(0xFF22C55E)
                            } else {
                                ElyraTheme.colors.textTertiary
                            }
                        )
                )

                Spacer(
                    modifier = Modifier.size(5.dp)
                )

                Text(
                    text =
                        if (device.isOn) {
                            "On"
                        } else {
                            "Off"
                        },
                    style = ElyraTheme.typography.labelSmall,
                    color =
                        if (device.isOn) {
                            Color(0xFF22C55E)
                        } else {
                            ElyraTheme.colors.textSecondary
                        }
                )
            }
        }


        // ------------------------------------------------------------
        // REMOVE DEVICE
        // ------------------------------------------------------------

        IconButton(
            onClick = onRemove
        ) {

            Icon(
                imageVector = Icons.Outlined.RemoveCircleOutline,
                contentDescription = "Remove device from room",
                tint = ElyraTheme.colors.error
            )
        }
    }
}


// ============================================================================
// EMPTY DEVICES
// ============================================================================

@Composable
private fun EmptyDevicesCard() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.Lightbulb,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = ElyraTheme.colors.textTertiary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "No devices in this room",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = "Devices can be assigned to this room later.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// SAVE BUTTON
// ============================================================================

@Composable
private fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                if (enabled) {
                    ElyraTheme.colors.primary
                } else {
                    ElyraTheme.colors.surfaceInteractive
                }
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Save Changes",
            style = ElyraTheme.typography.labelLarge,
            color =
                if (enabled) {
                    ElyraTheme.colors.onPrimary
                } else {
                    ElyraTheme.colors.textDisabled
                }
        )
    }
}


// ============================================================================
// DELETE ROOM
// ============================================================================

@Composable
private fun DeleteRoomButton(
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
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.DeleteOutline,
            contentDescription = "Delete room",
            tint = ElyraTheme.colors.error
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )

        Text(
            text = "Delete room",
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.error
        )
    }
}