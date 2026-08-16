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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.devices.DeviceViewModel

@Composable
fun EditRoomScreen(
    roomId: String,
    roomViewModel: RoomViewModel,
    deviceViewModel: DeviceViewModel,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {

    val roomState by roomViewModel.state.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(roomId) {
        roomViewModel.loadRoom(roomId)
        deviceViewModel.loadDevices()
    }

    var roomName by remember {
        mutableStateOf("")
    }

    LaunchedEffect(roomState.selectedRoom) {
        roomState.selectedRoom?.let {
            roomName = it.name
        }
    }

    LaunchedEffect(roomState.isSaved) {
        if (roomState.isSaved) {
            onSave()
            roomViewModel.consumeSaved()
        }
    }

    LaunchedEffect(roomState.isDeleted) {
        if (roomState.isDeleted) {
            onDelete()
            roomViewModel.consumeDeleted()
        }
    }

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

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
                                    deviceViewModel.updateDevice(
                                        device.copy(roomId = "")
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                if (roomState.error != null) {

                    Text(
                        text = roomState.error ?: "",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.error
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }

                SaveButton(
                    enabled = roomName.isNotBlank() && !roomState.isLoading,
                    onClick = {
                        roomViewModel.updateRoom(roomId, roomName.trim())
                    }
                )
            }

            item {

                DeleteRoomButton(
                    onClick = {
                        roomViewModel.deleteRoom(roomId)
                    }
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}

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

@Composable
private fun RoomDeviceCard(
    device: Device,
    onRemove: () -> Unit
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
                    if (isOn) {
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
                    if (isOn) {
                        Color(0xFF22C55E)
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = device.type.editRoomDisplayName(),
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
                            if (isOn) {
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
                        if (isOn) {
                            "On"
                        } else {
                            "Off"
                        },
                    style = ElyraTheme.typography.labelSmall,
                    color =
                        if (isOn) {
                            Color(0xFF22C55E)
                        } else {
                            ElyraTheme.colors.textSecondary
                        }
                )
            }
        }

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

private fun DeviceType.editRoomDisplayName(): String {

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
