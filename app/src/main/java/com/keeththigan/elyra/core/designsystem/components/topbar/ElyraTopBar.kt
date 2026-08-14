package com.keeththigan.elyra.core.designsystem.components.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.iconbutton.ElyraIconButton

/**
 * Standard Elyra top app bar.
 *
 * Provides a consistent navigation/header experience
 * across application screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElyraTopBar(
    title: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    navigationIcon: ImageVector? = null,
    navigationContentDescription: String? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = ElyraTheme.typography.titleLarge,
                color = ElyraTheme.colors.textPrimary
            )
        },

        navigationIcon = {
            if (
                navigationIcon != null &&
                onNavigationClick != null
            ) {
                ElyraIconButton(
                    imageVector = navigationIcon,
                    contentDescription =
                        navigationContentDescription ?: "Navigate back",
                    onClick = onNavigationClick
                )
            }
        },

        actions = actions,

        modifier = modifier,

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = ElyraTheme.colors.background,
            titleContentColor = ElyraTheme.colors.textPrimary,
            navigationIconContentColor = ElyraTheme.colors.textPrimary,
            actionIconContentColor = ElyraTheme.colors.textPrimary
        )
    )
}