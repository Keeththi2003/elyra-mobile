package com.keeththigan.elyra.feature.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme


@Composable
fun SettingsScreen(
    onAppearanceClick: () -> Unit = {}
) {

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp
            )
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================================================
        // HEADER
        // =========================================================

        Text(
            text = "Settings",
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Manage your Elyra experience.",
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // =========================================================
        // ACCOUNT
        // =========================================================

        SectionTitle("Account")

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SettingsCard {

            SettingsItem(
                icon = Icons.Outlined.Person,
                title = "Profile",
                subtitle = "Manage your account",
                onClick = {}
            )

            Divider()

            SettingsItem(
                icon = Icons.Outlined.Security,
                title = "Security",
                subtitle = "Password and account security",
                onClick = {}
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // =========================================================
        // PREFERENCES
        // =========================================================

        SectionTitle("Preferences")

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SettingsCard {

            SettingsSwitchItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                subtitle = "Receive device alerts",
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it
                }
            )

            Divider()

            SettingsItem(
                icon = Icons.Outlined.DarkMode,
                title = "Appearance",
                subtitle = "Light, dark, or system theme",
                onClick = onAppearanceClick
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        // =========================================================
        // ABOUT
        // =========================================================

        SectionTitle("About")

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SettingsCard {

            SettingsItem(
                icon = Icons.Outlined.Info,
                title = "About Elyra",
                subtitle = "Version 1.0.0",
                onClick = {}
            )
        }


        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Elyra",
            modifier = Modifier.fillMaxWidth(),
            style = ElyraTheme.typography.labelMedium,
            color = ElyraTheme.colors.textTertiary
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}


// ================================================================
// SECTION TITLE
// ================================================================

@Composable
private fun SectionTitle(
    text: String
) {

    Text(
        text = text,
        modifier = Modifier.padding(
            horizontal = 4.dp
        ),
        style = ElyraTheme.typography.labelLarge,
        color = ElyraTheme.colors.textSecondary
    )
}


// ================================================================
// SETTINGS CARD
// ================================================================

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
    ) {

        content()
    }
}


// ================================================================
// SETTINGS ITEM
// ================================================================

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon
        )

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ================================================================
// SWITCH ITEM
// ================================================================

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingsIcon(
            icon = icon
        )

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = subtitle,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}


// ================================================================
// ICON
// ================================================================

@Composable
private fun SettingsIcon(
    icon: ImageVector
) {

    Row(
        modifier = Modifier
            .size(42.dp)
            .clip(
                RoundedCornerShape(13.dp)
            )
            .background(
                ElyraTheme.colors.surfaceSecondary
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(21.dp),
            tint = ElyraTheme.colors.textPrimary
        )
    }
}


// ================================================================
// DIVIDER
// ================================================================

@Composable
private fun Divider() {

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                ElyraTheme.colors.borderSubtle
            )
            .padding(
                horizontal = 16.dp
            )
    )
}