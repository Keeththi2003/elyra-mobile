package com.keeththigan.elyra.core.designsystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Elyra Semantic Color System
 *
 * Semantic colors describe WHAT a color means,
 * not what the raw color is.
 *
 * Screens and components should use these tokens.
 *
 * Example:
 *
 * ElyraTheme.colors.background
 * ElyraTheme.colors.textPrimary
 * ElyraTheme.colors.primary
 *
 * Never use primitive colors directly inside
 * feature screens.
 */
@Immutable
data class ElyraSemanticColors(

    val background: Color,

    val backgroundSecondary: Color,

    val backgroundTertiary: Color,

    val surface: Color,

    val surfaceElevated: Color,

    val surfaceSecondary: Color,

    val surfaceInteractive: Color,

    val surfaceDisabled: Color,

    val textPrimary: Color,

    val textSecondary: Color,

    val textTertiary: Color,

    val textDisabled: Color,

    val textOnPrimary: Color,

    val border: Color,

    val borderSubtle: Color,

    val borderStrong: Color,

    val primary: Color,

    val primaryContainer: Color,

    val onPrimary: Color,

    val onPrimaryContainer: Color,

    val secondary: Color,

    val secondaryContainer: Color,

    val onSecondary: Color,

    val onSecondaryContainer: Color,

    val success: Color,

    val successContainer: Color,

    val onSuccess: Color,

    val onSuccessContainer: Color,

    val warning: Color,

    val warningContainer: Color,

    val onWarning: Color,

    val onWarningContainer: Color,

    val error: Color,

    val errorContainer: Color,

    val onError: Color,

    val onErrorContainer: Color,

    val info: Color,

    val infoContainer: Color,

    val onInfo: Color,

    val onInfoContainer: Color,

    /**
     * Device currently active.
     *
     * Example:
     * Light ON
     * Fan ON
     * AC running
     */
    val deviceActive: Color,

    /**
     * Device currently inactive.
     */
    val deviceInactive: Color,

    /**
     * Device unavailable / offline.
     */
    val deviceOffline: Color,

    /**
     * Device connecting / processing.
     */
    val devicePending: Color,

    val scrim: Color,

    val selection: Color
)
