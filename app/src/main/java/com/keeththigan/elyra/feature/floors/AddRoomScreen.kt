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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddRoomScreen(
    floorId: String,
    roomViewModel: RoomViewModel,
    onBack: () -> Unit,
    onRoomCreated: () -> Unit
) {

    val state by roomViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onRoomCreated()
            roomViewModel.consumeSaved()
        }
    }

    var roomName by remember {
        mutableStateOf("")
    }

    // Not persisted: the Room model has no description field.
    var roomDescription by remember {
        mutableStateOf("")
    }

    val canCreate =
        roomName.trim().isNotEmpty() && !state.isLoading

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
                    .clip(RoundedCornerShape(14.dp))
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
                    text = "Add Room",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Create a room for this floor",
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(22.dp)
                    )
                    .background(
                        ElyraTheme.colors.surface
                    )
                    .border(
                        width = 1.dp,
                        color = ElyraTheme.colors.border,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(20.dp)
            ) {

                Row(
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

                    Column {

                        Text(
                            text =
                                if (roomName.isBlank()) {
                                    "New Room"
                                } else {
                                    roomName
                                },
                            style = ElyraTheme.typography.titleMedium,
                            color = ElyraTheme.colors.textPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(3.dp)
                        )

                        Text(
                            text = "0 devices",
                            style = ElyraTheme.typography.bodySmall,
                            color = ElyraTheme.colors.textSecondary
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Room information",
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Enter the basic details for this room",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = "Room name",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            RoomTextField(
                value = roomName,
                onValueChange = {
                    roomName = it
                },
                placeholder = "e.g. Living Room"
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Description",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            RoomTextField(
                value = roomDescription,
                onValueChange = {
                    roomDescription = it
                },
                placeholder = "Optional description"
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(
                            RoundedCornerShape(11.dp)
                        )
                        .background(
                            ElyraTheme.colors.surface
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Apartment,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp),
                        tint = ElyraTheme.colors.textPrimary
                    )
                }

                Spacer(
                    modifier = Modifier.width(11.dp)
                )

                Column {

                    Text(
                        text = "Add devices later",
                        style = ElyraTheme.typography.labelLarge,
                        color = ElyraTheme.colors.textPrimary
                    )

                    Text(
                        text = "You can assign devices to this room after creating it.",
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }
        }

        if (state.error != null) {

            Text(
                text = state.error ?: "",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.error
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                )
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
                    roomViewModel.createRoom(roomName.trim(), floorId)
                },
            contentAlignment = Alignment.Center
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (canCreate) {

                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp),
                        tint = ElyraTheme.colors.onPrimary
                    )

                    Spacer(
                        modifier = Modifier.width(7.dp)
                    )
                }

                Text(
                    text = if (state.isLoading) "Creating…" else "Create Room",
                    style = ElyraTheme.typography.labelLarge,
                    color =
                        if (canCreate) {
                            ElyraTheme.colors.onPrimary
                        } else {
                            ElyraTheme.colors.textDisabled
                        }
                )
            }
        }
    }
}

@Composable
private fun RoomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {

    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        modifier = Modifier
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
