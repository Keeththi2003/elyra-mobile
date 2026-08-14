package com.keeththigan.elyra.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.keeththigan.elyra.core.designsystem.color.ElyraColors
import com.keeththigan.elyra.core.designsystem.color.ElyraSemanticColors
import com.keeththigan.elyra.core.designsystem.dimensions.ElyraDimensions
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing
import com.keeththigan.elyra.core.designsystem.typography.ElyraTypography

// ================================================================
// LIGHT COLORS
// ================================================================

private val ElyraLightColors = ElyraSemanticColors(

    // Background
    background = ElyraColors.Neutral50,
    backgroundSecondary = ElyraColors.Neutral100,
    backgroundTertiary = ElyraColors.Neutral150,

    // Surfaces
    surface = ElyraColors.Neutral0,
    surfaceElevated = ElyraColors.Neutral0,
    surfaceSecondary = ElyraColors.Neutral100,
    surfaceInteractive = ElyraColors.Neutral150,
    surfaceDisabled = ElyraColors.Neutral100,

    // Text
    textPrimary = ElyraColors.Neutral900,
    textSecondary = ElyraColors.Neutral600,
    textTertiary = ElyraColors.Neutral500,
    textDisabled = ElyraColors.Neutral400,
    textOnPrimary = ElyraColors.Neutral0,

    // Borders
    border = ElyraColors.Neutral200,
    borderSubtle = ElyraColors.Neutral150,
    borderStrong = ElyraColors.Neutral300,

    // Primary / Brand
    primary = ElyraColors.Accent600,
    primaryContainer = ElyraColors.Accent100,
    onPrimary = ElyraColors.Neutral0,
    onPrimaryContainer = ElyraColors.Accent900,

    // Secondary
    secondary = ElyraColors.Neutral700,
    secondaryContainer = ElyraColors.Neutral150,
    onSecondary = ElyraColors.Neutral0,
    onSecondaryContainer = ElyraColors.Neutral900,

    // Success
    success = ElyraColors.Green600,
    successContainer = ElyraColors.Green100,
    onSuccess = ElyraColors.Neutral0,
    onSuccessContainer = ElyraColors.Green900,

    // Warning
    warning = ElyraColors.Amber600,
    warningContainer = ElyraColors.Amber100,
    onWarning = ElyraColors.Neutral0,
    onWarningContainer = ElyraColors.Amber900,

    // Error
    error = ElyraColors.Red600,
    errorContainer = ElyraColors.Red100,
    onError = ElyraColors.Neutral0,
    onErrorContainer = ElyraColors.Red900,

    // Information
    info = ElyraColors.Blue600,
    infoContainer = ElyraColors.Blue100,
    onInfo = ElyraColors.Neutral0,
    onInfoContainer = ElyraColors.Blue900,

    // Device states
    deviceActive = ElyraColors.Green600,
    deviceInactive = ElyraColors.Neutral500,
    deviceOffline = ElyraColors.Red600,
    devicePending = ElyraColors.Amber600,

    // Special
    scrim = ElyraColors.Scrim,
    selection = ElyraColors.Accent100
)


// ================================================================
// DARK COLORS
// ================================================================

private val ElyraDarkColors = ElyraSemanticColors(

    // Background
    background = ElyraColors.Neutral1000,
    backgroundSecondary = ElyraColors.Neutral950,
    backgroundTertiary = ElyraColors.Neutral900,

    // Surfaces
    surface = ElyraColors.Neutral900,
    surfaceElevated = ElyraColors.Neutral850,
    surfaceSecondary = ElyraColors.Neutral850,
    surfaceInteractive = ElyraColors.Neutral800,
    surfaceDisabled = ElyraColors.Neutral850,

    // Text
    textPrimary = ElyraColors.Neutral50,
    textSecondary = ElyraColors.Neutral300,
    textTertiary = ElyraColors.Neutral400,
    textDisabled = ElyraColors.Neutral600,
    textOnPrimary = ElyraColors.Neutral950,

    // Borders
    border = ElyraColors.Neutral700,
    borderSubtle = ElyraColors.Neutral800,
    borderStrong = ElyraColors.Neutral600,

    // Primary / Brand
    primary = ElyraColors.Accent400,
    primaryContainer = ElyraColors.Accent800,
    onPrimary = ElyraColors.Neutral950,
    onPrimaryContainer = ElyraColors.Accent100,

    // Secondary
    secondary = ElyraColors.Neutral300,
    secondaryContainer = ElyraColors.Neutral800,
    onSecondary = ElyraColors.Neutral950,
    onSecondaryContainer = ElyraColors.Neutral100,

    // Success
    success = ElyraColors.Green500,
    successContainer = ElyraColors.Green900,
    onSuccess = ElyraColors.Neutral950,
    onSuccessContainer = ElyraColors.Green100,

    // Warning
    warning = ElyraColors.Amber500,
    warningContainer = ElyraColors.Amber900,
    onWarning = ElyraColors.Neutral950,
    onWarningContainer = ElyraColors.Amber100,

    // Error
    error = ElyraColors.Red500,
    errorContainer = ElyraColors.Red900,
    onError = ElyraColors.Neutral0,
    onErrorContainer = ElyraColors.Red100,

    // Information
    info = ElyraColors.Blue500,
    infoContainer = ElyraColors.Blue900,
    onInfo = ElyraColors.Neutral0,
    onInfoContainer = ElyraColors.Blue100,

    // Device states
    deviceActive = ElyraColors.Green500,
    deviceInactive = ElyraColors.Neutral500,
    deviceOffline = ElyraColors.Red500,
    devicePending = ElyraColors.Amber500,

    // Special
    scrim = ElyraColors.Scrim,
    selection = ElyraColors.Accent800
)


// ================================================================
// COMPOSITION LOCAL
// ================================================================

private val LocalElyraColors =
    staticCompositionLocalOf<ElyraSemanticColors> {
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
    val colors = if (darkTheme) {
        ElyraDarkColors
    } else {
        ElyraLightColors
    }

    val materialColorScheme = if (darkTheme) {

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
// THEME ACCESS
// ================================================================

object ElyraTheme {

    val colors: ElyraSemanticColors
        @Composable
        @ReadOnlyComposable
        get() = LocalElyraColors.current

    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val spacing
        get() = ElyraSpacing

    val dimension
        get() = ElyraDimensions
}