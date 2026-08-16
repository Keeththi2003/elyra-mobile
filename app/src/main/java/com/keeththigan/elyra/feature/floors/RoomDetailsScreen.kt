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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.topbar.ElyraDetailTopBar
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.devices.DeviceViewModel

@Composable
fun RoomDetailsScreen(
    roomId: String,
    roomViewModel: RoomViewModel,
    floorViewModel: FloorViewModel,
    deviceViewModel: DeviceViewModel,
    onBack: () -> Unit,
    onDeviceClick: (String) -> Unit,
    onEditRoom: () -> Unit,
    onAddDevice: (floorId: String, roomId: String) -> Unit
) {

    val roomState by roomViewModel.state.collectAsStateWithLifecycle()
    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(roomId) {
        roomViewModel.loadRoom(roomId)
        deviceViewModel.loadDevices()
    }

    LaunchedEffect(roomState.selectedRoom?.floorId) {
        roomState.selectedRoom?.floorId?.takeIf { it.isNotBlank() }?.let {
            floorViewModel.loadFloor(it)
        }
    }

    val roomName = roomState.selectedRoom?.name ?: ""
    val floorName = floorState.selectedFloor?.name ?: ""

    val devices =
        deviceState.devices.filter {
            it.roomId == roomId
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
    ) {

        ElyraDetailTopBar(
            title = roomName.ifBlank { "Room" },
            subtitle = floorName.ifBlank { null },
            onBack = onBack,
            actions = {
                IconButton(onClick = onEditRoom) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit room",
                        modifier = Modifier.size(20.dp),
                        tint = ElyraTheme.colors.textSecondary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = 20.dp
                ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            if (roomState.error != null || deviceState.error != null) {

                item {

                    Text(
                        text = roomState.error ?: deviceState.error ?: "",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.error
                    )
                }
            }

            item {

                RoomSummaryCard(
                    roomName = roomName,
                    floorName = floorName,
                    deviceCount = devices.size
                )
            }

            item {

                Column {

                    Text(
                        text = "Devices",
                        style = ElyraTheme.typography.titleMedium,
                        color = ElyraTheme.colors.textPrimary
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "${devices.size} devices connected to this room",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }

            if (devices.isEmpty()) {

                item {

                    EmptyRoomDevices(
                        onAddDevice = {
                            onAddDevice(
                                roomState.selectedRoom?.floorId.orEmpty(),
                                roomId
                            )
                        }
                    )
                }

            } else {

                items(
                    count = devices.size
                ) { index ->

                    val device = devices[index]

                    RoomDeviceCard(
                        device = device,
                        onClick = {
                            onDeviceClick(device.id)
                        }
                    )
                }
            }

            item {

                if (devices.isNotEmpty()) {

                    AddDeviceCard(
                        onClick = {
                            onAddDevice(
                                roomState.selectedRoom?.floorId.orEmpty(),
                                roomId
                            )
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}

@Composable
private fun RoomSummaryCard(
    roomName: String,
    floorName: String,
    deviceCount: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(
                    RoundedCornerShape(17.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = roomName,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = floorName,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "$deviceCount devices",
                style = ElyraTheme.typography.labelSmall,
                color = ElyraTheme.colors.textSecondary
            )
        }
    }
}

@Composable
private fun RoomDeviceCard(
    device: Device,
    onClick: () -> Unit
) {

    val isOn = device.status == DeviceStatus.ON

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 15.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        DeviceIcon(
            type = device.type
        )

        Spacer(
            modifier = Modifier.width(12.dp)
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
                text = device.type.roomDeviceDisplayName(),
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    if (isOn) {
                        ElyraTheme.colors.success.copy(
                            alpha = 0.12f
                        )
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                )
                .padding(
                    horizontal = 9.dp,
                    vertical = 5.dp
                )
        ) {

            Text(
                text = if (isOn) "ON" else "OFF",
                style = ElyraTheme.typography.labelSmall,
                color =
                    if (isOn) {
                        ElyraTheme.colors.success
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = "Open device",
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}

@Composable
private fun DeviceIcon(
    type: DeviceType
) {

    val icon: ImageVector =
        when (type) {

            DeviceType.LIGHT ->
                Icons.Outlined.Lightbulb

            DeviceType.OUTLET ->
                Icons.Outlined.Power

            DeviceType.MULTI_SWITCH ->
                Icons.Outlined.Tune

            DeviceType.SAFETY_APPLIANCE ->
                Icons.Outlined.Power

            DeviceType.SECURITY_CAMERA ->
                Icons.Outlined.Power
        }

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
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(21.dp),
            tint = ElyraTheme.colors.textPrimary
        )
    }
}

@Composable
private fun EmptyRoomDevices(
    onAddDevice: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.surface
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "No devices yet",
            style = ElyraTheme.typography.titleSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Add a device to start managing this room.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        AddDeviceCard(
            onClick = onAddDevice
        )
    }
}

@Composable
private fun AddDeviceCard(
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
                horizontal = 15.dp,
                vertical = 13.dp
            )
            .navigationBarsPadding(),
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
            modifier = Modifier.width(11.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Add device",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "Connect a device to this room",
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

private fun DeviceType.roomDeviceDisplayName(): String {

    return when (this) {

        DeviceType.LIGHT ->
            "Light"

        DeviceType.OUTLET ->
            "Outlet"

        DeviceType.MULTI_SWITCH ->
            "Multi Switch"

        DeviceType.SAFETY_APPLIANCE ->
            "Safety Appliance"

        DeviceType.SECURITY_CAMERA ->
            "Security Camera"
    }
}
