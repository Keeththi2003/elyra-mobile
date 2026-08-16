package com.keeththigan.elyra.feature.floors.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.data.model.Room

/**
 * Abstract floor plan: rooms as zones on a two-column grid rather than a true
 * architectural layout, so no per-home asset is needed and it stays legible on
 * a phone. Each zone shows live device state, so this replaces the room list.
 */
@Composable
fun FloorPlanGrid(
    rooms: List<Room>,
    devices: List<Device>,
    modifier: Modifier = Modifier,
    onRoomClick: (String) -> Unit = {},
    onDeviceClick: (String) -> Unit = {},
    onAddRoom: () -> Unit = {}
) {

    Column(modifier = modifier.fillMaxWidth()) {

        rooms.chunked(2).forEach { rowRooms ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowRooms.forEach { room ->

                    RoomZone(
                        room = room,
                        devices = devices.filter { it.roomId == room.id },
                        modifier = Modifier.weight(1f),
                        onClick = { onRoomClick(room.id) },
                        onDeviceClick = onDeviceClick
                    )
                }

                // Keeps a lone zone at half width so the plan stays gridded.
                if (rowRooms.size == 1) {
                    AddZone(
                        modifier = Modifier.weight(1f),
                        onClick = onAddRoom
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // A full row of rooms still needs somewhere to add the next one.
        if (rooms.isEmpty() || rooms.size % 2 == 0) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                AddZone(
                    modifier = Modifier.weight(1f),
                    onClick = onAddRoom
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun RoomZone(
    room: Room,
    devices: List<Device>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDeviceClick: (String) -> Unit
) {

    val activeCount = devices.count { it.status == DeviceStatus.ON }

    val faultCount =
        devices.count {
            it.status == DeviceStatus.ERROR ||
                it.status == DeviceStatus.DISCONNECTED
        }

    val isLive = activeCount > 0

    Column(
        modifier = modifier
            .height(168.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isLive) {
                    ElyraTheme.colors.surfaceSecondary
                } else {
                    ElyraTheme.colors.surface
                }
            )
            .border(
                width = if (isLive) 1.5.dp else 1.dp,
                color = when {
                    faultCount > 0 -> ElyraTheme.colors.error
                    isLive -> ElyraTheme.colors.textPrimary
                    else -> ElyraTheme.colors.borderSubtle
                },
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = room.name,
                modifier = Modifier.weight(1f),
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary,
                maxLines = 1
            )

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                tint = ElyraTheme.colors.textTertiary
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = when {
                devices.isEmpty() -> "Empty room"
                faultCount > 0 -> "$faultCount need attention"
                else -> "$activeCount of ${devices.size} on"
            },
            style = ElyraTheme.typography.bodySmall,
            color = if (faultCount > 0) {
                ElyraTheme.colors.error
            } else {
                ElyraTheme.colors.textSecondary
            },
            maxLines = 1
        )

        Spacer(modifier = Modifier.weight(1f))


        if (devices.isEmpty()) {

            Text(
                text = "Tap to add devices",
                style = ElyraTheme.typography.labelSmall,
                color = ElyraTheme.colors.textTertiary
            )

        } else {

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                devices.take(4).forEach { device ->

                    DeviceChip(
                        device = device,
                        onClick = { onDeviceClick(device.id) }
                    )
                }

                if (devices.size > 4) {

                    Text(
                        text = "+${devices.size - 4}",
                        style = ElyraTheme.typography.labelSmall,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceChip(
    device: Device,
    onClick: () -> Unit
) {

    val on = device.status == DeviceStatus.ON

    val faulted =
        device.status == DeviceStatus.ERROR ||
            device.status == DeviceStatus.DISCONNECTED

    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    faulted -> ElyraTheme.colors.errorContainer
                    on -> ElyraTheme.colors.primary
                    else -> ElyraTheme.colors.surfaceInteractive
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = device.type.planIcon(),
            contentDescription = device.name,
            modifier = Modifier.size(15.dp),
            tint = when {
                faulted -> ElyraTheme.colors.error
                on -> ElyraTheme.colors.onPrimary
                else -> ElyraTheme.colors.textSecondary
            }
        )
    }
}

@Composable
private fun AddZone(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .height(168.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.border,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Add room",
                modifier = Modifier.size(17.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Add room",
            style = ElyraTheme.typography.labelMedium,
            color = ElyraTheme.colors.textSecondary
        )
    }
}

private fun DeviceType.planIcon(): ImageVector =
    when (this) {
        DeviceType.LIGHT -> Icons.Outlined.Lightbulb
        DeviceType.OUTLET -> Icons.Outlined.Power
        DeviceType.MULTI_SWITCH -> Icons.Outlined.Tune
        DeviceType.SAFETY_APPLIANCE -> Icons.Outlined.Security
        DeviceType.SECURITY_CAMERA -> Icons.Outlined.CameraAlt
    }
