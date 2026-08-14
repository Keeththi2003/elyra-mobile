package com.keeththigan.elyra.core.designsystem.spacing

import androidx.compose.ui.unit.dp

/**
 * Elyra Design System
 *
 * Central spacing scale used throughout the application.
 *
 * The goal is to maintain a consistent visual rhythm
 * across screens, cards, lists, forms and navigation.
 *
 * Avoid arbitrary spacing values inside feature UI.
 *
 * Example:
 *
 * Instead of:
 *
 * padding(19.dp)
 *
 * use:
 *
 * padding(ElyraSpacing.md)
 */
object ElyraSpacing {

    // ============================================================
    // BASE SPACING SCALE
    // ============================================================

    /**
     * 2dp
     *
     * Extremely small visual adjustments.
     */
    val xxs = 2.dp

    /**
     * 4dp
     *
     * Tight spacing between closely related elements.
     */
    val xs = 4.dp

    /**
     * 8dp
     *
     * Small internal spacing.
     */
    val sm = 8.dp

    /**
     * 12dp
     *
     * Compact component spacing.
     */
    val md = 12.dp

    /**
     * 16dp
     *
     * Standard spacing.
     *
     * Commonly used for:
     * - screen horizontal padding
     * - card content padding
     * - form fields
     */
    val lg = 16.dp

    /**
     * 20dp
     *
     * Comfortable spacing between related sections.
     */
    val xl = 20.dp

    /**
     * 24dp
     *
     * Standard section spacing.
     */
    val xxl = 24.dp

    /**
     * 32dp
     *
     * Large section separation.
     */
    val xxxl = 32.dp

    /**
     * 40dp
     *
     * Large visual separation.
     */
    val huge = 40.dp

    /**
     * 48dp
     *
     * Very large spacing.
     */
    val massive = 48.dp

    /**
     * 64dp
     *
     * Hero / major layout spacing.
     */
    val hero = 64.dp


    // ============================================================
    // SCREEN
    // ============================================================

    /**
     * Standard horizontal screen padding.
     */
    val screenHorizontal = 20.dp

    /**
     * Large screen horizontal padding.
     *
     * Useful on tablets and larger layouts.
     */
    val screenHorizontalLarge = 24.dp

    /**
     * Standard top spacing after a top bar.
     */
    val screenTop = 16.dp

    /**
     * Standard bottom spacing.
     */
    val screenBottom = 24.dp


    // ============================================================
    // COMPONENTS
    // ============================================================

    /**
     * Standard card content padding.
     */
    val cardPadding = 16.dp

    /**
     * Compact card padding.
     */
    val cardPaddingCompact = 12.dp

    /**
     * Standard button horizontal padding.
     */
    val buttonHorizontal = 20.dp

    /**
     * Standard button vertical padding.
     */
    val buttonVertical = 12.dp

    /**
     * Text field internal horizontal padding.
     */
    val inputHorizontal = 16.dp

    /**
     * Text field internal vertical padding.
     */
    val inputVertical = 14.dp


    // ============================================================
    // LISTS
    // ============================================================

    /**
     * Space between list items.
     */
    val listItemGap = 8.dp

    /**
     * Space between major list sections.
     */
    val listSectionGap = 24.dp


    // ============================================================
    // CONTENT
    // ============================================================

    /**
     * Small gap between icon and text.
     */
    val iconText = 8.dp

    /**
     * Gap between title and subtitle.
     */
    val titleSubtitle = 4.dp

    /**
     * Gap between related pieces of content.
     */
    val contentGap = 12.dp

    /**
     * Gap between major content blocks.
     */
    val sectionGap = 24.dp


    // ============================================================
    // BOTTOM NAVIGATION
    // ============================================================

    /**
     * Bottom navigation content padding.
     */
    val navigationHorizontal = 16.dp

    /**
     * Bottom navigation vertical padding.
     */
    val navigationVertical = 8.dp
}