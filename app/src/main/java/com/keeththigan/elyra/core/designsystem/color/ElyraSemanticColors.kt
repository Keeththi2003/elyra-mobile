package com.keeththigan.elyra.core.designsystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Elyra Design System
 *
 * Semantic colors describe the PURPOSE of a color in the UI.
 *
 * Architecture:
 *
 * Primitive Colors
 *       ↓
 * Semantic Colors
 *       ↓
 * Components
 *       ↓
 * Screens
 *
 * Feature screens should use semantic colors instead of
 * directly accessing primitive color tokens.
 */
@Immutable
data class ElyraSemanticColors(

    // ============================================================
    // BACKGROUND
    // ============================================================

    val background: Color,

    val backgroundSecondary: Color,

    val backgroundTertiary: Color,


    // ============================================================
    // SURFACES
    // ============================================================

    val surface: Color,

    val surfaceElevated: Color,

    val surfaceSecondary: Color,

    val surfaceInteractive: Color,

    val surfaceDisabled: Color,


    // ============================================================
    // CONTENT / TEXT
    // ============================================================

    val textPrimary: Color,

    val textSecondary: Color,

    val textTertiary: Color,

    val textDisabled: Color,

    val textOnPrimary: Color,


    // ============================================================
    // BORDERS / DIVIDERS
    // ============================================================

    val border: Color,

    val borderSubtle: Color,

    val borderStrong: Color,


    // ============================================================
    // BRAND / PRIMARY
    // ============================================================

    val primary: Color,

    val primaryContainer: Color,

    val onPrimary: Color,

    val onPrimaryContainer: Color,


    // ============================================================
    // SECONDARY / TECHNOLOGY ACCENT
    // ============================================================

    val secondary: Color,

    val secondaryContainer: Color,

    val onSecondary: Color,

    val onSecondaryContainer: Color,


    // ============================================================
    // SUCCESS
    // ============================================================

    val success: Color,

    val successContainer: Color,

    val onSuccess: Color,

    val onSuccessContainer: Color,


    // ============================================================
    // WARNING
    // ============================================================

    val warning: Color,

    val warningContainer: Color,

    val onWarning: Color,

    val onWarningContainer: Color,


    // ============================================================
    // ERROR
    // ============================================================

    val error: Color,

    val errorContainer: Color,

    val onError: Color,

    val onErrorContainer: Color,


    // ============================================================
    // INFORMATION
    // ============================================================

    val info: Color,

    val infoContainer: Color,

    val onInfo: Color,

    val onInfoContainer: Color,


    // ============================================================
    // DEVICE STATES
    // ============================================================

    /**
     * Device is currently active.
     *
     * Example:
     * Light ON
     * Fan ON
     * AC running
     */
    val deviceActive: Color,

    /**
     * Device is currently inactive.
     */
    val deviceInactive: Color,

    /**
     * Device is unavailable/offline.
     */
    val deviceOffline: Color,

    /**
     * Device is connecting or processing.
     */
    val devicePending: Color,


    // ============================================================
    // SPECIAL UI
    // ============================================================

    val scrim: Color,

    val selection: Color
)