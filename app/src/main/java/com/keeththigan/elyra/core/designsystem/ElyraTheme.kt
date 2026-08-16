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

private val ElyraLightColors = ElyraSemanticColors(

    /*
     * Grouped-background model: the page sits on a soft neutral and cards
     * are pure white on top of it. Previously both were white, so every
     * card melted into the page and the light theme read as flat.
     */
    background = ElyraColors.Neutral100,

    backgroundSecondary = ElyraColors.Neutral150,

    backgroundTertiary = ElyraColors.Neutral200,

    surface = ElyraColors.White,

    surfaceElevated = ElyraColors.White,

    surfaceSecondary = ElyraColors.Neutral150,

    surfaceInteractive = ElyraColors.Neutral200,

    surfaceDisabled = ElyraColors.Neutral150,

    textPrimary = ElyraColors.Black,

    textSecondary = ElyraColors.Neutral600,

    textTertiary = ElyraColors.Neutral500,

    textDisabled = ElyraColors.Neutral400,

    textOnPrimary = ElyraColors.White,

    border = ElyraColors.Neutral200,

    borderSubtle = ElyraColors.Neutral150,

    borderStrong = ElyraColors.Neutral300,

    primary = ElyraColors.Black,

    primaryContainer = ElyraColors.Neutral100,

    onPrimary = ElyraColors.White,

    onPrimaryContainer = ElyraColors.Black,

    secondary = ElyraColors.Neutral600,

    secondaryContainer = ElyraColors.Neutral100,

    onSecondary = ElyraColors.White,

    onSecondaryContainer = ElyraColors.Black,

    success = ElyraColors.Green600,

    successContainer = ElyraColors.Green100,

    onSuccess = ElyraColors.White,

    onSuccessContainer = ElyraColors.Green900,

    warning = ElyraColors.Amber600,

    warningContainer = ElyraColors.Amber100,

    onWarning = ElyraColors.White,

    onWarningContainer = ElyraColors.Amber900,

    error = ElyraColors.Red600,

    errorContainer = ElyraColors.Red100,

    onError = ElyraColors.White,

    onErrorContainer = ElyraColors.Red900,

    info = ElyraColors.Blue600,

    infoContainer = ElyraColors.Blue100,

    onInfo = ElyraColors.White,

    onInfoContainer = ElyraColors.Blue900,

    deviceActive = ElyraColors.Green600,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red600,

    devicePending = ElyraColors.Amber600,

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Neutral200
)

private val ElyraDarkColors = ElyraSemanticColors(

    background = ElyraColors.Black,

    backgroundSecondary = ElyraColors.Neutral950,

    backgroundTertiary = ElyraColors.Neutral900,

    /*
     * Lifted one step off pure black so cards separate from the page
     * instead of merging with it.
     */
    surface = ElyraColors.Neutral900,

    surfaceElevated = ElyraColors.Neutral850,

    surfaceSecondary = ElyraColors.Neutral850,

    surfaceInteractive = ElyraColors.Neutral800,

    surfaceDisabled = ElyraColors.Neutral900,

    textPrimary = ElyraColors.White,

    textSecondary = ElyraColors.Neutral400,

    textTertiary = ElyraColors.Neutral500,

    textDisabled = ElyraColors.Neutral700,

    textOnPrimary = ElyraColors.Black,

    border = ElyraColors.Neutral800,

    borderSubtle = ElyraColors.Neutral900,

    borderStrong = ElyraColors.Neutral700,

    primary = ElyraColors.White,

    primaryContainer = ElyraColors.Neutral800,

    onPrimary = ElyraColors.Black,

    onPrimaryContainer = ElyraColors.White,

    secondary = ElyraColors.Neutral400,

    secondaryContainer = ElyraColors.Neutral900,

    onSecondary = ElyraColors.Black,

    onSecondaryContainer = ElyraColors.White,

    success = ElyraColors.Green500,

    successContainer = ElyraColors.Green900,

    onSuccess = ElyraColors.Black,

    onSuccessContainer = ElyraColors.Green100,

    warning = ElyraColors.Amber500,

    warningContainer = ElyraColors.Amber900,

    onWarning = ElyraColors.Black,

    onWarningContainer = ElyraColors.Amber100,

    error = ElyraColors.Red500,

    errorContainer = ElyraColors.Red900,

    onError = ElyraColors.White,

    onErrorContainer = ElyraColors.Red100,

    info = ElyraColors.Blue500,

    infoContainer = ElyraColors.Blue900,

    onInfo = ElyraColors.White,

    onInfoContainer = ElyraColors.Blue100,

    deviceActive = ElyraColors.Green500,

    deviceInactive = ElyraColors.Neutral500,

    deviceOffline = ElyraColors.Red500,

    devicePending = ElyraColors.Amber500,

    scrim = ElyraColors.Scrim,

    selection = ElyraColors.Neutral800
)

private val LocalElyraColors =
    staticCompositionLocalOf<ElyraSemanticColors> {
        ElyraLightColors
    }

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
