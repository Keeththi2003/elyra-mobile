package com.keeththigan.elyra.core.designsystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Elyra Design System
 *
 * Central shape system.
 *
 * Shapes are intentionally restrained to create
 * a consistent and premium visual language.
 *
 * Avoid arbitrary corner radii inside feature screens.
 */
object ElyraShapes {

    // ============================================================
    // BASE RADII
    // ============================================================

    /**
     * 4dp
     *
     * Very subtle rounding.
     */
    val xs = 4.dp

    /**
     * 8dp
     *
     * Compact controls and small surfaces.
     */
    val sm = 8.dp

    /**
     * 12dp
     *
     * Inputs, smaller cards and controls.
     */
    val md = 12.dp

    /**
     * 16dp
     *
     * Primary cards and larger controls.
     */
    val lg = 16.dp

    /**
     * 20dp
     *
     * Prominent cards and elevated surfaces.
     */
    val xl = 20.dp

    /**
     * 24dp
     *
     * Large containers and sheets.
     */
    val xxl = 24.dp

    /**
     * Fully rounded shape.
     *
     * Useful for pills, badges and compact status indicators.
     */
    val full = 999.dp


    // ============================================================
    // COMPONENT SHAPES
    // ============================================================

    /**
     * Standard card shape.
     */
    val card = RoundedCornerShape(lg)

    /**
     * Large feature card.
     */
    val featureCard = RoundedCornerShape(xl)

    /**
     * Compact card.
     */
    val compactCard = RoundedCornerShape(md)

    /**
     * Text field / input shape.
     */
    val input = RoundedCornerShape(md)

    /**
     * Standard button shape.
     */
    val button = RoundedCornerShape(md)

    /**
     * Small button.
     */
    val buttonSmall = RoundedCornerShape(sm)

    /**
     * Pill shape.
     */
    val pill = RoundedCornerShape(full)

    /**
     * Dialog shape.
     */
    val dialog = RoundedCornerShape(xxl)

    /**
     * Bottom sheet shape.
     */
    val bottomSheet = RoundedCornerShape(
        topStart = xxl,
        topEnd = xxl,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )


    // ============================================================
    // MATERIAL 3 SHAPE SYSTEM
    // ============================================================

    /**
     * Shapes mapped into Material 3's shape system.
     *
     * This allows Material components to inherit
     * Elyra's shape language through the theme.
     */
    val material = Shapes(

        extraSmall = RoundedCornerShape(xs),

        small = RoundedCornerShape(sm),

        medium = RoundedCornerShape(md),

        large = RoundedCornerShape(lg),

        extraLarge = RoundedCornerShape(xxl)
    )
}