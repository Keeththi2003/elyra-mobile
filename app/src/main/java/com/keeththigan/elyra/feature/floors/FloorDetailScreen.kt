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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.topbar.ElyraDetailTopBar
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.floors.components.FloorPlanGrid

// ============================================================================
// ROOM MODEL
// ============================================================================

private data class ElyraRoom(
    val id: String,
    val name: String,
    val deviceCount: Int,
    val activeDeviceCount: Int
)


// ============================================================================
// FLOOR DETAIL SCREEN
// ============================================================================

@Composable
fun FloorDetailScreen(
    floorId: String,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel,
    deviceViewModel: DeviceViewModel,
    onBack: () -> Unit,
    onRoomClick: (String) -> Unit,
    onDeviceClick: (String) -> Unit = {},
    onAddRoom: () -> Unit,
    onEditFloor: () -> Unit,
    onDeleteFloor: () -> Unit = {}
) {

    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(floorId) {
        floorViewModel.loadFloor(floorId)
        roomViewModel.loadRoomsForFloor(floorId)
        deviceViewModel.loadDevices()
    }

    val floorName = floorState.selectedFloor?.name ?: ""

    val rooms =
        roomState.rooms.filter { it.floorId == floorId }.map { room ->

            ElyraRoom(
                id = room.id,
                name = room.name,
                deviceCount = deviceState.devices.count {
                    it.roomId == room.id
                },
                activeDeviceCount = deviceState.devices.count {
                    it.roomId == room.id && it.status == DeviceStatus.ON
                }
            )
        }

    val totalDevices =
        rooms.sumOf {
            it.deviceCount
        }

    val activeDevices =
        rooms.sumOf {
            it.activeDeviceCount
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .padding(
                horizontal = 20.dp
            )
    ) {

        // ====================================================================
        // TOP BAR
        // ====================================================================

        ElyraDetailTopBar(
            title = floorName.ifBlank { "Floor" },
            subtitle = "Floor overview",
            onBack = onBack,
            // The screen's Column already insets content by 20.dp.
            horizontalPadding = 0.dp,
            actions = {
                IconButton(onClick = onEditFloor) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit floor",
                        modifier = Modifier.size(20.dp),
                        tint = ElyraTheme.colors.textSecondary
                    )
                }
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (floorState.error != null || roomState.error != null || deviceState.error != null) {

            Text(
                text = floorState.error ?: roomState.error ?: deviceState.error ?: "",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.error
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }


        // ====================================================================
        // FLOOR SUMMARY
        // ====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            FloorStatCard(
                value = rooms.size.toString(),
                label = "Rooms",
                modifier = Modifier.weight(1f)
            )

            FloorStatCard(
                value = totalDevices.toString(),
                label = "Devices",
                modifier = Modifier.weight(1f)
            )

            FloorStatCard(
                value = activeDevices.toString(),
                label = "Active",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // ====================================================================
        // FLOOR PLAN
        //
        // The plan is the only room listing on this screen — a separate list
        // below it showed exactly the same rooms twice.
        // ====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "Floor plan",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Tap a room to open it, or a device to control it",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {

                FloorPlanGrid(
                    rooms = roomState.rooms.filter { it.floorId == floorId },
                    devices = deviceState.devices,
                    onRoomClick = onRoomClick,
                    onDeviceClick = onDeviceClick,
                    onAddRoom = onAddRoom
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                DeleteFloorButton(onClick = onDeleteFloor)

                Spacer(
                    modifier = Modifier.height(28.dp)
                )
            }
        }
    }
}


// ============================================================================
// DELETE FLOOR
// ============================================================================

@Composable
private fun DeleteFloorButton(
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

        Column {

            Text(
                text = "Delete floor",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.error
            )

            Text(
                text = "Removes its rooms; devices become unassigned",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}


// ============================================================================
// FLOOR STAT CARD
// ============================================================================

@Composable
private fun FloorStatCard(
    value: String,
    label: String,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(
                vertical = 17.dp,
                horizontal = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            style = ElyraTheme.typography.titleLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            style = ElyraTheme.typography.labelMedium,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// ROOM CARD
// ============================================================================

@Composable
private fun RoomCard(
    room: ElyraRoom,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
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

        // ====================================================================
        // ROOM ICON
        // ====================================================================

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.MeetingRoom,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )


        // ====================================================================
        // ROOM INFO
        // ====================================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = room.name,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "${room.deviceCount} devices",
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
                            Color(0xFF22C55E)
                        )
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = "${room.activeDeviceCount} active",
                    style = ElyraTheme.typography.labelMedium,
                    color = Color(0xFF22C55E)
                )
            }
        }


        // ====================================================================
        // ARROW
        // ====================================================================

        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = "Open ${room.name}",
            modifier = Modifier.size(16.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}