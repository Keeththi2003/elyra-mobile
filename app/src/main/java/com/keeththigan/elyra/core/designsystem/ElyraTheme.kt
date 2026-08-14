package com.keeththigan.elyra.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.keeththigan.elyra.core.designsystem.color.ElyraColors
import com.keeththigan.elyra.core.designsystem.color.ElyraSemanticColors
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes
import com.keeththigan.elyra.core.designsystem.typography.ElyraTypography

/**
 * Elyra Design System Theme.
 *
 * Central entry point for:
 *
 * - Colors
 * - Typography
 * - Shapes
 * - Spacing
 * - Light / Dark mode
 *
 * Usage:
 *
 * ElyraTheme {
 *     App()
 * }
 */

// ================================================================
// LIGHT THEME
// ================================================================

private val ElyraLightColors = ElyraSemanticColors(

    // ------------------------------------------------------------
    // BACKGROUND
    // ------------------------------------------------------------

    background = ElyraColors.Neutral50,

    backgroundSecondary = ElyraColors.Neutral100,

    backgroundTertiary = ElyraColors.Neutral150,


    // ------------------------------------------------------------
    // SURFACES
    // ------------------------------------------------------------

    surface = ElyraColors.Neutral0,

    surfaceElevated = ElyraColors.Neutral0,

    surfaceSecondary = ElyraColors.Neutral100,

    surfaceInteractive = ElyraColors.Neutral150,

    surfaceDisabled = ElyraColors.Neutral100,


    // ------------------------------------------------------------
    // TEXT
    // ------------------------------------------------------------

    textPrimary = ElyraColors.Neutral900,

    textSecondary = ElyraColors.Neutral600,

    textTertiary = ElyraColors.Neutral500,

    textDisabled = ElyraColors.Neutral400,

    textOnPrimary = ElyraColors.Neutral0,


    // ------------------------------------------------------------
    // BORDERS
    // ------------------------------------------------------------

    border = ElyraColors.Neutral200,

    borderSubtle = ElyraColors.Neutral150,

    borderStrong = ElyraColors.Neutral300,


    // ------------------------------------------------------------
    // PRIMARY
    // ------------------------------------------------------------

    primary = ElyraColors.Indigo600,

    primaryContainer = ElyraColors.Indigo100,

    onPrimary = ElyraColors.Neutral0,

    onPrimaryContainer = ElyraColors.Indigo900,


    // ------------------------------------------------------------
    // SECONDARY
    // ------------------------------------------------------------

    secondary = ElyraColors.Cyan600,

    secondaryContainer = ElyraColors.Cyan100,

    onSecondary = ElyraColors.Neutral0,

    onSecondaryContainer = ElyraColors.Cyan900,


    // ------------------------------------------------------------
    // SUCCESS
    // ------------------------------------------------------------

    success = ElyraColors.Green600,

    successContainer = ElyraColors.Green100,

    onSuccess = ElyraColors.Neutral0,

    onSuccessContainer = ElyraColors.Green900,


    // ------------------------------------------------------------
    // WARNING
    // ------------------------------------------------------------

    warning = ElyraColors.Amber600,

    warningContainer = ElyraColors.Amber100,

    onWarning = ElyraColors.Neutral0,

    onWarningContainer = ElyraColors.Amber900,


    // ------------------------------------------------------------
    // ERROR
    // ------------------------------------------------------------

    error = ElyraColors.Red600,

    errorContainer = ElyraColors.Red100,

    onError = ElyraColors.Neutral0,

    onErrorContainer = ElyraColors.Red900,


    // ------------------------------------------------------------
    // INFORMATION
    // ------------------------------------------------------------

    info = ElyraColors.Blue600,

    infoContainer = ElyraColors.Blue100,

    onInfo = ElyraColors.Neutral0,

    onInfoContainer = ElyraColors.Blue900,


    // ------------------------------------------------------------
    // DEVICE STATES
    // ------------------------------------------------------------

    deviceActive = ElyraColors.Green600,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red600,

    devicePending = ElyraColors.Amber600,


    // ------------------------------------------------------------
    // SPECIAL
    // ------------------------------------------------------------

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Indigo100
)


// ================================================================
// DARK THEME
// ================================================================

private val ElyraDarkColors = ElyraSemanticColors(

    // ------------------------------------------------------------
    // BACKGROUND
    // ------------------------------------------------------------

    background = ElyraColors.Neutral1000,

    backgroundSecondary = ElyraColors.Neutral950,

    backgroundTertiary = ElyraColors.Neutral900,


    // ------------------------------------------------------------
    // SURFACES
    // ------------------------------------------------------------

    surface = ElyraColors.Neutral900,

    surfaceElevated = ElyraColors.Neutral850,

    surfaceSecondary = ElyraColors.Neutral850,

    surfaceInteractive = ElyraColors.Neutral800,

    surfaceDisabled = ElyraColors.Neutral850,


    // ------------------------------------------------------------
    // TEXT
    // ------------------------------------------------------------

    textPrimary = ElyraColors.Neutral50,

    textSecondary = ElyraColors.Neutral300,

    textTertiary = ElyraColors.Neutral400,

    textDisabled = ElyraColors.Neutral600,

    textOnPrimary = ElyraColors.Neutral0,


    // ------------------------------------------------------------
    // BORDERS
    // ------------------------------------------------------------

    border = ElyraColors.Neutral700,

    borderSubtle = ElyraColors.Neutral800,

    borderStrong = ElyraColors.Neutral600,


    // ------------------------------------------------------------
    // PRIMARY
    // ------------------------------------------------------------

    primary = ElyraColors.Indigo400,

    primaryContainer = ElyraColors.Indigo800,

    onPrimary = ElyraColors.Neutral950,

    onPrimaryContainer = ElyraColors.Indigo100,


    // ------------------------------------------------------------
    // SECONDARY
    // ------------------------------------------------------------

    secondary = ElyraColors.Cyan400,

    secondaryContainer = ElyraColors.Cyan800,

    onSecondary = ElyraColors.Neutral950,

    onSecondaryContainer = ElyraColors.Cyan100,


    // ------------------------------------------------------------
    // SUCCESS
    // ------------------------------------------------------------

    success = ElyraColors.Green500,

    successContainer = ElyraColors.Green900,

    onSuccess = ElyraColors.Neutral950,

    onSuccessContainer = ElyraColors.Green100,


    // ------------------------------------------------------------
    // WARNING
    // ------------------------------------------------------------

    warning = ElyraColors.Amber500,

    warningContainer = ElyraColors.Amber900,

    onWarning = ElyraColors.Neutral950,

    onWarningContainer = ElyraColors.Amber100,


    // ------------------------------------------------------------
    // ERROR
    // ------------------------------------------------------------

    error = ElyraColors.Red500,

    errorContainer = ElyraColors.Red900,

    onError = ElyraColors.Neutral0,

    onErrorContainer = ElyraColors.Red100,


    // ------------------------------------------------------------
    // INFORMATION
    // ------------------------------------------------------------

    info = ElyraColors.Blue500,

    infoContainer = ElyraColors.Blue900,

    onInfo = ElyraColors.Neutral0,

    onInfoContainer = ElyraColors.Blue100,


    // ------------------------------------------------------------
    // DEVICE STATES
    // ------------------------------------------------------------

    deviceActive = ElyraColors.Green500,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red500,

    devicePending = ElyraColors.Amber500,


    // ------------------------------------------------------------
    // SPECIAL
    // ------------------------------------------------------------

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Indigo800
)


// ================================================================
// COMPOSITION LOCAL
// ================================================================

private val LocalElyraColors = staticCompositionLocalOf<ElyraSemanticColors> {
    ElyraLightColors
}


// ================================================================
// THEME
// ================================================================

@Composable
fun ElyraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme) {
            ElyraDarkColors
        } else {
            ElyraLightColors
        }

    val materialColorScheme =
        if (darkTheme) {

            darkColorScheme(
                primary = colors.primary,
                onPrimary = colors.onPrimary,
                primaryContainer = colors.primaryContainer,
                onPrimaryContainer = colors.onPrimaryContainer,

                secondary = colors.secondary,
                onSecondary = colors.onSecondary,
                secondaryContainer = colors.secondaryContainer,
                onSecondaryContainer = colors.onSecondaryContainer,

                background = colors.background,
                onBackground = colors.textPrimary,

                surface = colors.surface,
                onSurface = colors.textPrimary,

                surfaceVariant = colors.surfaceSecondary,
                onSurfaceVariant = colors.textSecondary,

                error = colors.error,
                onError = colors.onError,
                errorContainer = colors.errorContainer,
                onErrorContainer = colors.onErrorContainer
            )

        } else {

            lightColorScheme(
                primary = colors.primary,
                onPrimary = colors.onPrimary,
                primaryContainer = colors.primaryContainer,
                onPrimaryContainer = colors.onPrimaryContainer,

                secondary = colors.secondary,
                onSecondary = colors.onSecondary,
                secondaryContainer = colors.secondaryContainer,
                onSecondaryContainer = colors.onSecondaryContainer,

                background = colors.background,
                onBackground = colors.textPrimary,

                surface = colors.surface,
                onSurface = colors.textPrimary,

                surfaceVariant = colors.surfaceSecondary,
                onSurfaceVariant = colors.textSecondary,

                error = colors.error,
                onError = colors.onError,
                errorContainer = colors.errorContainer,
                onErrorContainer = colors.onErrorContainer
            )
        }

    androidx.compose.runtime.CompositionLocalProvider(
        LocalElyraColors provides colors
    ) {

        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = ElyraTypography,
            shapes = ElyraShapes.material,
            content = content
        )
    }
}


// ================================================================
// ELYRA THEME ACCESS
// ================================================================

object ElyraTheme {

    /**
     * Elyra semantic colors.
     *
     * Example:
     *
     * ElyraTheme.colors.primary
     */
    val colors: ElyraSemanticColors
        @Composable
        @ReadOnlyComposable
        get() = LocalElyraColors.current


    /**
     * Elyra typography.
     *
     * Example:
     *
     * ElyraTheme.typography.headlineLarge
     */
    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography


    /**
     * Elyra shapes.
     *
     * Example:
     *
     * ElyraTheme.shapes.large
     */
    val shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes


    /**
     * Elyra spacing.
     *
     * Example:
     *
     * ElyraTheme.spacing.lg
     */
    val spacing
        get() = ElyraSpacing
}