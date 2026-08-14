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


// ============================================================================
// LIGHT THEME
// ============================================================================

private val ElyraLightColors = ElyraSemanticColors(

    // ------------------------------------------------------------------------
    // BACKGROUND
    // ------------------------------------------------------------------------

    background = ElyraColors.White,

    backgroundSecondary = ElyraColors.Neutral50,

    backgroundTertiary = ElyraColors.Neutral100,


    // ------------------------------------------------------------------------
    // SURFACES
    // ------------------------------------------------------------------------

    surface = ElyraColors.White,

    surfaceElevated = ElyraColors.White,

    surfaceSecondary = ElyraColors.Neutral50,

    surfaceInteractive = ElyraColors.Neutral100,

    surfaceDisabled = ElyraColors.Neutral150,


    // ------------------------------------------------------------------------
    // TEXT
    // ------------------------------------------------------------------------

    textPrimary = ElyraColors.Black,

    textSecondary = ElyraColors.Neutral600,

    textTertiary = ElyraColors.Neutral500,

    textDisabled = ElyraColors.Neutral400,

    textOnPrimary = ElyraColors.White,


    // ------------------------------------------------------------------------
    // BORDERS
    // ------------------------------------------------------------------------

    border = ElyraColors.Neutral200,

    borderSubtle = ElyraColors.Neutral150,

    borderStrong = ElyraColors.Neutral300,


    // ------------------------------------------------------------------------
    // PRIMARY
    //
    // BLACK
    // ------------------------------------------------------------------------

    primary = ElyraColors.Black,

    primaryContainer = ElyraColors.Neutral100,

    onPrimary = ElyraColors.White,

    onPrimaryContainer = ElyraColors.Black,


    // ------------------------------------------------------------------------
    // SECONDARY
    //
    // Neutral gray.
    // ------------------------------------------------------------------------

    secondary = ElyraColors.Neutral600,

    secondaryContainer = ElyraColors.Neutral100,

    onSecondary = ElyraColors.White,

    onSecondaryContainer = ElyraColors.Black,


    // ------------------------------------------------------------------------
    // SUCCESS
    // ------------------------------------------------------------------------

    success = ElyraColors.Green600,

    successContainer = ElyraColors.Green100,

    onSuccess = ElyraColors.White,

    onSuccessContainer = ElyraColors.Green900,


    // ------------------------------------------------------------------------
    // WARNING
    // ------------------------------------------------------------------------

    warning = ElyraColors.Amber600,

    warningContainer = ElyraColors.Amber100,

    onWarning = ElyraColors.White,

    onWarningContainer = ElyraColors.Amber900,


    // ------------------------------------------------------------------------
    // ERROR
    // ------------------------------------------------------------------------

    error = ElyraColors.Red600,

    errorContainer = ElyraColors.Red100,

    onError = ElyraColors.White,

    onErrorContainer = ElyraColors.Red900,


    // ------------------------------------------------------------------------
    // INFORMATION
    // ------------------------------------------------------------------------

    info = ElyraColors.Blue600,

    infoContainer = ElyraColors.Blue100,

    onInfo = ElyraColors.White,

    onInfoContainer = ElyraColors.Blue900,


    // ------------------------------------------------------------------------
    // DEVICE STATES
    // ------------------------------------------------------------------------

    deviceActive = ElyraColors.Green600,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red600,

    devicePending = ElyraColors.Amber600,


    // ------------------------------------------------------------------------
    // SPECIAL
    // ------------------------------------------------------------------------

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Neutral200
)


// ============================================================================
// DARK THEME
// ============================================================================

private val ElyraDarkColors = ElyraSemanticColors(

    // ------------------------------------------------------------------------
    // BACKGROUND
    // ------------------------------------------------------------------------

    background = ElyraColors.Black,

    backgroundSecondary = ElyraColors.Neutral950,

    backgroundTertiary = ElyraColors.Neutral900,


    // ------------------------------------------------------------------------
    // SURFACES
    // ------------------------------------------------------------------------

    surface = ElyraColors.Neutral950,

    surfaceElevated = ElyraColors.Neutral900,

    surfaceSecondary = ElyraColors.Neutral900,

    surfaceInteractive = ElyraColors.Neutral800,

    surfaceDisabled = ElyraColors.Neutral900,


    // ------------------------------------------------------------------------
    // TEXT
    // ------------------------------------------------------------------------

    textPrimary = ElyraColors.White,

    textSecondary = ElyraColors.Neutral400,

    textTertiary = ElyraColors.Neutral500,

    textDisabled = ElyraColors.Neutral700,

    textOnPrimary = ElyraColors.Black,


    // ------------------------------------------------------------------------
    // BORDERS
    // ------------------------------------------------------------------------

    border = ElyraColors.Neutral800,

    borderSubtle = ElyraColors.Neutral900,

    borderStrong = ElyraColors.Neutral700,


    // ------------------------------------------------------------------------
    // PRIMARY
    //
    // WHITE
    // ------------------------------------------------------------------------

    primary = ElyraColors.White,

    primaryContainer = ElyraColors.Neutral800,

    onPrimary = ElyraColors.Black,

    onPrimaryContainer = ElyraColors.White,


    // ------------------------------------------------------------------------
    // SECONDARY
    // ------------------------------------------------------------------------

    secondary = ElyraColors.Neutral400,

    secondaryContainer = ElyraColors.Neutral900,

    onSecondary = ElyraColors.Black,

    onSecondaryContainer = ElyraColors.White,


    // ------------------------------------------------------------------------
    // SUCCESS
    // ------------------------------------------------------------------------

    success = ElyraColors.Green500,

    successContainer = ElyraColors.Green900,

    onSuccess = ElyraColors.Black,

    onSuccessContainer = ElyraColors.Green100,


    // ------------------------------------------------------------------------
    // WARNING
    // ------------------------------------------------------------------------

    warning = ElyraColors.Amber500,

    warningContainer = ElyraColors.Amber900,

    onWarning = ElyraColors.Black,

    onWarningContainer = ElyraColors.Amber100,


    // ------------------------------------------------------------------------
    // ERROR
    // ------------------------------------------------------------------------

    error = ElyraColors.Red500,

    errorContainer = ElyraColors.Red900,

    onError = ElyraColors.White,

    onErrorContainer = ElyraColors.Red100,


    // ------------------------------------------------------------------------
    // INFORMATION
    // ------------------------------------------------------------------------

    info = ElyraColors.Blue500,

    infoContainer = ElyraColors.Blue900,

    onInfo = ElyraColors.White,

    onInfoContainer = ElyraColors.Blue100,


    // ------------------------------------------------------------------------
    // DEVICE STATES
    // ------------------------------------------------------------------------

    deviceActive = ElyraColors.Green500,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red500,

    devicePending = ElyraColors.Amber500,


    // ------------------------------------------------------------------------
    // SPECIAL
    // ------------------------------------------------------------------------

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Neutral800
)


// ============================================================================
// COMPOSITION LOCAL
// ============================================================================

private val LocalElyraColors =
    staticCompositionLocalOf<ElyraSemanticColors> {
        ElyraLightColors
    }


// ============================================================================
// ELYRA THEME
// ============================================================================

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


    // ========================================================================
    // MATERIAL 3 COLOR SCHEME
    // ========================================================================

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


    // ========================================================================
    // PROVIDE ELYRA DESIGN TOKENS
    // ========================================================================

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


// ============================================================================
// ELYRA THEME ACCESS
// ============================================================================

object ElyraTheme {

    /**
     * Semantic Elyra colors.
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
     */
    val typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography


    /**
     * Elyra shapes.
     */
    val shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes


    /**
     * Elyra spacing.
     */
    val spacing
        get() = ElyraSpacing


    /**
     * Elyra dimensions.
     */
    val dimension
        get() = ElyraDimensions
}