package com.keeththigan.elyra.core.designsystem.color

import androidx.compose.ui.graphics.Color

/**
 * Elyra Design System
 *
 * Primitive color tokens.
 *
 * These colors should NOT be used directly inside feature screens.
 * Features should consume semantic colors provided by the Elyra theme.
 *
 * Architecture:
 *
 * Primitive → Semantic → Component → UI
 */
object ElyraColors {

    // ============================================================
    // NEUTRAL PALETTE
    // ============================================================

    /**
     * Light surfaces
     */
    val Neutral0 = Color(0xFFFFFFFF)
    val Neutral50 = Color(0xFFF8F8FA)
    val Neutral100 = Color(0xFFF2F2F5)
    val Neutral150 = Color(0xFFEAEAEE)
    val Neutral200 = Color(0xFFE2E2E7)

    /**
     * Mid neutrals
     */
    val Neutral300 = Color(0xFFD1D1D6)
    val Neutral400 = Color(0xFFAEAEB2)
    val Neutral500 = Color(0xFF8E8E93)
    val Neutral600 = Color(0xFF6E6E73)

    /**
     * Dark neutrals
     */
    val Neutral700 = Color(0xFF48484A)
    val Neutral800 = Color(0xFF2C2C2E)
    val Neutral850 = Color(0xFF1F2024)
    val Neutral900 = Color(0xFF15161A)
    val Neutral950 = Color(0xFF0D0E11)
    val Neutral1000 = Color(0xFF08090B)


    // ============================================================
    // ELYRA BRAND PALETTE
    // ============================================================

    /**
     * Primary Elyra brand color.
     *
     * Used for important actions, selected states,
     * navigation emphasis and brand moments.
     */
    val Indigo50 = Color(0xFFF3F1FF)
    val Indigo100 = Color(0xFFE8E5FF)
    val Indigo200 = Color(0xFFD4CFFF)
    val Indigo300 = Color(0xFFB9B0FF)
    val Indigo400 = Color(0xFF9789FF)
    val Indigo500 = Color(0xFF7868F2)
    val Indigo600 = Color(0xFF6958E8)
    val Indigo700 = Color(0xFF5848D0)
    val Indigo800 = Color(0xFF473BA8)
    val Indigo900 = Color(0xFF382F82)


    // ============================================================
    // SECONDARY ACCENT
    // ============================================================

    /**
     * A subtle cyan accent.
     *
     * Used sparingly for technology-oriented states,
     * energy information and selected visual highlights.
     */
    val Cyan50 = Color(0xFFECFCFF)
    val Cyan100 = Color(0xFFD4F7FC)
    val Cyan200 = Color(0xFFAAEDF5)
    val Cyan300 = Color(0xFF78DFEB)
    val Cyan400 = Color(0xFF4BCEDC)
    val Cyan500 = Color(0xFF28B8C8)
    val Cyan600 = Color(0xFF1C9AAA)
    val Cyan700 = Color(0xFF197C89)
    val Cyan800 = Color(0xFF1A6370)
    val Cyan900 = Color(0xFF1A525D)


    // ============================================================
    // SEMANTIC STATUS COLORS
    // ============================================================

    /**
     * Success
     *
     * Device online
     * Device active
     * Operation successful
     */
    val Green50 = Color(0xFFF0FDF4)
    val Green100 = Color(0xFFDCFCE7)
    val Green500 = Color(0xFF22C55E)
    val Green600 = Color(0xFF16A34A)
    val Green700 = Color(0xFF15803D)
    val Green900 = Color(0xFF14532D)


    /**
     * Warning
     *
     * Attention required
     * Device warning
     * Scheduled event approaching
     */
    val Amber50 = Color(0xFFFFFBEB)
    val Amber100 = Color(0xFFFEF3C7)
    val Amber500 = Color(0xFFF59E0B)
    val Amber600 = Color(0xFFD97706)
    val Amber700 = Color(0xFFB45309)
    val Amber900 = Color(0xFF78350F)


    /**
     * Error
     *
     * Device failure
     * Connection failure
     * Destructive action
     */
    val Red50 = Color(0xFFFEF2F2)
    val Red100 = Color(0xFFFEE2E2)
    val Red500 = Color(0xFFEF4444)
    val Red600 = Color(0xFFDC2626)
    val Red700 = Color(0xFFB91C1C)
    val Red900 = Color(0xFF7F1D1D)


    /**
     * Information
     */
    val Blue50 = Color(0xFFEFF6FF)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue500 = Color(0xFF3B82F6)
    val Blue600 = Color(0xFF2563EB)
    val Blue700 = Color(0xFF1D4ED8)
    val Blue900 = Color(0xFF1E3A8A)


    // ============================================================
    // OVERLAY / SCRIM
    // ============================================================

    /**
     * Used for modal surfaces, bottom sheets and dialogs.
     */
    val Scrim = Color(0x99000000)

    /**
     * Subtle white overlay for dark-mode interaction states.
     */
    val WhiteOverlay8 = Color(0x14FFFFFF)
    val WhiteOverlay12 = Color(0x1FFFFFFF)
    val WhiteOverlay16 = Color(0x29FFFFFF)

    /**
     * Subtle black overlay for light-mode interaction states.
     */
    val BlackOverlay4 = Color(0x0A000000)
    val BlackOverlay8 = Color(0x14000000)
    val BlackOverlay12 = Color(0x1F000000)
}