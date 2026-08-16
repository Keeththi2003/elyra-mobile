package com.keeththigan.elyra.feature.floors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.Room

/**
 * Abstract grid mapping of a floor.
 *
 * Rooms are laid out on a simple two-column grid rather than a true
 * architectural plan: it gives the spatial overview the brief asks for while
 * staying readable on a phone and needing no per-home floor-plan asset. Each
 * cell reflects live device state, so the plan doubles as a status overview.
 */
@Composable
fun FloorPlanGrid(
    rooms: List<Room>,
    devices: List<Device>,
    modifier: Modifier = Modifier,
    onRoomClick: (String) -> Unit = {}
) {

    if (rooms.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {

        rooms.chunked(2).forEach { rowRooms ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowRooms.forEach { room ->

                    val roomDevices =
                        devices.filter { it.roomId == room.id }

                    FloorPlanCell(
                        name = room.name,
                        deviceCount = roomDevices.size,
                        activeCount = roomDevices.count {
                            it.status == DeviceStatus.ON
                        },
                        hasFault = roomDevices.any {
                            it.status == DeviceStatus.ERROR ||
                                it.status == DeviceStatus.DISCONNECTED
                        },
                        modifier = Modifier.weight(1f),
                        onClick = { onRoomClick(room.id) }
                    )
                }

                // Keeps a lone room at half width so the grid stays square.
                if (rowRooms.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Composable
private fun FloorPlanCell(
    name: String,
    deviceCount: Int,
    activeCount: Int,
    hasFault: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val isLive = activeCount > 0

    Column(
        modifier = modifier
            .aspectRatio(1.25f)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (isLive) {
                    ElyraTheme.colors.surfaceSecondary
                } else {
                    ElyraTheme.colors.surface
                }
            )
            .border(
                width = if (isLive) 1.5.dp else 1.dp,
                color = if (isLive) {
                    ElyraTheme.colors.textPrimary
                } else {
                    ElyraTheme.colors.borderSubtle
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Outlined.MeetingRoom,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = ElyraTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            if (hasFault) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ElyraTheme.colors.error)
                )

            } else if (isLive) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(ElyraTheme.colors.success)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = name,
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = if (deviceCount == 0) {
                "No devices"
            } else {
                "$activeCount of $deviceCount on"
            },
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary,
            maxLines = 1
        )
    }
}
