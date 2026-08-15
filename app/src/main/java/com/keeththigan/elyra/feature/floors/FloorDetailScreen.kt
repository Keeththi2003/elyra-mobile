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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

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
// SAMPLE ROOMS
// ============================================================================

private val sampleRooms = listOf(

    ElyraRoom(
        id = "living_room",
        name = "Living Room",
        deviceCount = 2,
        activeDeviceCount = 1
    ),

    ElyraRoom(
        id = "kitchen",
        name = "Kitchen",
        deviceCount = 2,
        activeDeviceCount = 1
    ),

    ElyraRoom(
        id = "entrance",
        name = "Entrance",
        deviceCount = 1,
        activeDeviceCount = 1
    )
)


// ============================================================================
// FLOOR DETAIL SCREEN
// ============================================================================

@Composable
fun FloorDetailScreen(
    floorId: String,
    onBack: () -> Unit,
    onRoomClick: (String) -> Unit,
    onAddRoom: () -> Unit,
    onEditFloor: () -> Unit
) {

    // Later this will come from your repository/database.
    val floorName = remember(floorId) {
        when (floorId) {
            "first" -> "First Floor"
            else -> "Ground Floor"
        }
    }

    val totalDevices =
        sampleRooms.sumOf {
            it.deviceCount
        }

    val activeDevices =
        sampleRooms.sumOf {
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

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
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

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = floorName,
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Floor overview",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            IconButton(
                onClick = onEditFloor
            ) {

                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit floor",
                    modifier = Modifier.size(21.dp),
                    tint = ElyraTheme.colors.textSecondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ====================================================================
        // FLOOR SUMMARY
        // ====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            FloorStatCard(
                value = sampleRooms.size.toString(),
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
        // ROOMS HEADER
        // ====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Rooms",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "Devices organized by room",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            IconButton(
                onClick = onAddRoom,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.primary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add room",
                    tint = ElyraTheme.colors.onPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // ====================================================================
        // ROOM LIST
        // ====================================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(
                items = sampleRooms,
                key = {
                    it.id
                }
            ) { room ->

                RoomCard(
                    room = room,
                    onClick = {
                        onRoomClick(room.id)
                    }
                )
            }

            item {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
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