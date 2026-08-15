package com.keeththigan.elyra.feature.devices

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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

@Composable
fun EditDeviceScreen(
    deviceId: String,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {

    // Temporary data.
    // Later load using:
    //
    // deviceId -> ViewModel -> Repository -> Database

    var deviceName by remember {
        mutableStateOf("Living Room Light")
    }

    var deviceType by remember {
        mutableStateOf("Light")
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
                    text = "Edit Device",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = "Update device information",
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
            // DEVICE PREVIEW
            // =============================================================

            item {

                DeviceHeader(
                    deviceName = deviceName,
                    deviceType = deviceType
                )
            }

            // =============================================================
            // DEVICE INFORMATION
            // =============================================================

            item {

                SectionTitle(
                    title = "Device information",
                    subtitle = "Update the device details"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                FormField(
                    value = deviceName,
                    onValueChange = {
                        deviceName = it
                    },
                    placeholder = "Device name"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                FormField(
                    value = deviceType,
                    onValueChange = {
                        deviceType = it
                    },
                    placeholder = "Device type"
                )
            }

            // =============================================================
            // DEVICE ID
            // =============================================================

            item {

                SectionTitle(
                    title = "Device identifier",
                    subtitle = "This value identifies the device"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            ElyraTheme.colors.surface
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = "Device ID",
                            style = ElyraTheme.typography.labelMedium,
                            color = ElyraTheme.colors.textSecondary
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = deviceId,
                            style = ElyraTheme.typography.bodyMedium,
                            color = ElyraTheme.colors.textPrimary
                        )
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            if (deviceName.isNotBlank()) {
                                ElyraTheme.colors.primary
                            } else {
                                ElyraTheme.colors.surfaceInteractive
                            }
                        )
                        .clickable(
                            enabled = deviceName.isNotBlank(),
                            onClick = onSave
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Save Changes",
                        style = ElyraTheme.typography.labelLarge,
                        color =
                            if (deviceName.isNotBlank()) {
                                ElyraTheme.colors.onPrimary
                            } else {
                                ElyraTheme.colors.textDisabled
                            }
                    )
                }
            }

            // =============================================================
            // DELETE
            // =============================================================

            item {

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
                            onClick = onDelete
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 15.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete device",
                        tint = ElyraTheme.colors.error
                    )

                    Spacer(
                        modifier = Modifier.size(10.dp)
                    )

                    Text(
                        text = "Delete device",
                        style = ElyraTheme.typography.labelLarge,
                        color = ElyraTheme.colors.error
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
// DEVICE HEADER
// ============================================================================

@Composable
private fun DeviceHeader(
    deviceName: String,
    deviceType: String
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
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Devices,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column {

            Text(
                text = deviceName,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = deviceType,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
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
                    .padding(horizontal = 16.dp),
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