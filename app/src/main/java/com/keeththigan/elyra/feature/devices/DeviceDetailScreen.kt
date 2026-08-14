package com.keeththigan.elyra.feature.devices

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Wifi
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
fun DeviceDetailScreen(
    deviceName: String,
    onBack: () -> Unit
) {

    var isOn by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .navigationBarsPadding()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(horizontal = 20.dp)
    ) {

        // =========================================================
        // TOP BAR
        // =========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = ElyraTheme.colors.textPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =========================================================
        // DEVICE HEADER
        // =========================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(
                        if (isOn) {
                            ElyraTheme.colors.primary
                        } else {
                            ElyraTheme.colors.surfaceSecondary
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint =
                        if (isOn) {
                            ElyraTheme.colors.onPrimary
                        } else {
                            ElyraTheme.colors.textSecondary
                        }
                )
            }

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = deviceName,
                    style = ElyraTheme.typography.headlineSmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isOn) {
                                    ElyraTheme.colors.success
                                } else {
                                    ElyraTheme.colors.deviceInactive
                                }
                            )
                    )

                    Spacer(
                        modifier = Modifier.size(7.dp)
                    )

                    Text(
                        text =
                            if (isOn) {
                                "On"
                            } else {
                                "Off"
                            },
                        style = ElyraTheme.typography.bodySmall,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        // =========================================================
        // POWER CONTROL
        // =========================================================

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(24.dp)
                )
                .background(
                    ElyraTheme.colors.surface
                )
                .border(
                    width = 1.dp,
                    color = ElyraTheme.colors.borderSubtle,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Power",
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(
                        if (isOn) {
                            ElyraTheme.colors.primary
                        } else {
                            ElyraTheme.colors.surfaceSecondary
                        }
                    )
                    .border(
                        width = 1.dp,
                        color =
                            if (isOn) {
                                ElyraTheme.colors.primary
                            } else {
                                ElyraTheme.colors.border
                            },
                        shape = CircleShape
                    )
                    .clickable {
                        isOn = !isOn
                    },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.PowerSettingsNew,
                    contentDescription = "Power",
                    modifier = Modifier.size(42.dp),
                    tint =
                        if (isOn) {
                            ElyraTheme.colors.onPrimary
                        } else {
                            ElyraTheme.colors.textSecondary
                        }
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text =
                    if (isOn) {
                        "Device is on"
                    } else {
                        "Device is off"
                    },
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    if (isOn) {
                        "Tap the button to turn it off"
                    } else {
                        "Tap the button to turn it on"
                    },
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // =========================================================
        // DEVICE INFORMATION
        // =========================================================

        Text(
            text = "Device information",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        DeviceInfoCard(
            icon = Icons.Outlined.Wifi,
            title = "Connection",
            value = "Connected"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        DeviceInfoCard(
            icon = Icons.Outlined.Lightbulb,
            title = "Device type",
            value = "Smart light"
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}


// ================================================================
// DEVICE INFO CARD
// ================================================================

@Composable
private fun DeviceInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
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
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
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

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = ElyraTheme.typography.labelMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = value,
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}