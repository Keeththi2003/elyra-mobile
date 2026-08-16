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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.feature.devices.DeviceViewModel

private data class EditRoomUi(
    val id: String,
    val name: String,
    val deviceCount: Int
)

@Composable
fun EditFloorScreen(
    floorId: String,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel,
    deviceViewModel: DeviceViewModel,
    onBack: () -> Unit,
    onAddRoom: () -> Unit,
    onRoomClick: (String) -> Unit,
    onSave: () -> Unit
) {

    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(floorId) {
        floorViewModel.loadFloor(floorId)
        roomViewModel.loadRoomsForFloor(floorId)
        deviceViewModel.loadDevices()
    }

    var floorName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(floorState.selectedFloor) {
        floorState.selectedFloor?.let {
            floorName = it.name
        }
    }

    LaunchedEffect(floorState.isSaved) {
        if (floorState.isSaved) {
            onSave()
            floorViewModel.consumeSaved()
        }
    }

    val rooms =
        roomState.rooms.filter { it.floorId == floorId }.map { room ->

            EditRoomUi(
                id = room.id,
                name = room.name,
                deviceCount = deviceState.devices.count {
                    it.roomId == room.id
                }
            )
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
    ) {

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
                    text = "Edit Floor",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Update floor information",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {

                SectionHeader(
                    title = "Floor information",
                    subtitle = "Change the name of this floor"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                FloorNameField(
                    value = floorName,
                    onValueChange = {
                        floorName = it
                    }
                )
            }

            item {

                SectionHeader(
                    title = "Rooms",
                    subtitle = "${rooms.size} rooms on this floor"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    rooms.forEach { room ->

                        EditRoomCard(
                            room = room,

                            onClick = {
                                onRoomClick(room.id)
                            },

                            onDelete = {
                                roomViewModel.deleteRoom(room.id)
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                AddRoomButton(
                    onClick = onAddRoom
                )
            }

            if (floorState.error != null) {

                item {

                    Text(
                        text = floorState.error ?: "",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.error
                    )
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                val canSave =
                    floorName.isNotBlank() && !floorState.isLoading

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            if (canSave) {
                                ElyraTheme.colors.primary
                            } else {
                                ElyraTheme.colors.surfaceInteractive
                            }
                        )
                        .clickable(
                            enabled = canSave
                        ) {
                            floorViewModel.updateFloor(floorId, floorName.trim())
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = if (floorState.isLoading) "Saving…" else "Save Changes",
                        style = ElyraTheme.typography.labelLarge,
                        color =
                            if (canSave) {
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

@Composable
private fun FloorNameField(
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
                    .padding(
                        horizontal = 16.dp
                    ),
                contentAlignment = Alignment.CenterStart
            ) {

                if (value.isEmpty()) {

                    Text(
                        text = "Floor name",
                        style = ElyraTheme.typography.bodyLarge,
                        color = ElyraTheme.colors.textTertiary
                    )
                }

                innerTextField()
            }
        }
    )
}

@Composable
private fun EditRoomCard(
    room: EditRoomUi,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(17.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 14.dp,
                vertical = 13.dp
            ),
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
                modifier = Modifier.size(22.dp),
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

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = "${room.deviceCount} devices",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        IconButton(
            onClick = onDelete
        ) {

            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "Delete room",
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Open room",
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}

@Composable
private fun AddRoomButton(
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
                text = "Add room",
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "Add another room to this floor",
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
