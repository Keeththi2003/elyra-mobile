package com.keeththigan.elyra.feature.settings.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

enum class AppearanceOption {
    SYSTEM,
    LIGHT,
    DARK
}

@Composable
fun AppearanceScreen(
    selectedOption: AppearanceOption = AppearanceOption.SYSTEM,
    onOptionSelected: (AppearanceOption) -> Unit = {},
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(horizontal = 20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
                    .clickable(
                        onClick = onBack
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "‹",
                    style = ElyraTheme.typography.headlineMedium,
                    color = ElyraTheme.colors.textPrimary
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "Appearance",
                style = ElyraTheme.typography.titleLarge,
                color = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Choose your look",
            style = ElyraTheme.typography.headlineMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Elyra will use your preferred appearance across the app.",
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        AppearanceOptionCard(
            icon = Icons.Outlined.SettingsBrightness,
            title = "System",
            description = "Follow your device settings",
            selected = selectedOption == AppearanceOption.SYSTEM,
            onClick = {
                onOptionSelected(
                    AppearanceOption.SYSTEM
                )
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        AppearanceOptionCard(
            icon = Icons.Outlined.LightMode,
            title = "Light",
            description = "Use the light appearance",
            selected = selectedOption == AppearanceOption.LIGHT,
            onClick = {
                onOptionSelected(
                    AppearanceOption.LIGHT
                )
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        AppearanceOptionCard(
            icon = Icons.Outlined.DarkMode,
            title = "Dark",
            description = "Use the dark appearance",
            selected = selectedOption == AppearanceOption.DARK,
            onClick = {
                onOptionSelected(
                    AppearanceOption.DARK
                )
            }
        )
    }
}

@Composable
private fun AppearanceOptionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    val containerColor =
        if (selected) {
            ElyraTheme.colors.primary
        } else {
            ElyraTheme.colors.surface
        }

    val contentColor =
        if (selected) {
            ElyraTheme.colors.onPrimary
        } else {
            ElyraTheme.colors.textPrimary
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(containerColor)
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 18.dp,
                vertical = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .size(48.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .background(
                    if (selected) {
                        ElyraTheme.colors.onPrimary.copy(
                            alpha = 0.12f
                        )
                    } else {
                        ElyraTheme.colors.surfaceSecondary
                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(23.dp),
                tint = contentColor
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
                style = ElyraTheme.typography.titleMedium,
                color = contentColor
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = description,
                style = ElyraTheme.typography.bodySmall,
                color =
                    if (selected) {
                        ElyraTheme.colors.onPrimary.copy(
                            alpha = 0.72f
                        )
                    } else {
                        ElyraTheme.colors.textSecondary
                    }
            )
        }

        if (selected) {

            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = "Selected",
                modifier = Modifier.size(22.dp),
                tint = ElyraTheme.colors.onPrimary
            )
        }
    }
}
