package com.keeththigan.elyra.core.designsystem.color

import androidx.compose.ui.graphics.Color

/**
 * Elyra primitive color tokens.
 *
 * These are the raw building blocks of the Elyra design system.
 *
 * Feature screens should normally use ElyraSemanticColors
 * instead of accessing these values directly.
 */
object ElyraColors {

    // ============================================================
    // NEUTRALS
    // ============================================================

    val Neutral0 = Color(0xFFFFFFFF)

    val Neutral50 = Color(0xFFFAFAFA)

    val Neutral100 = Color(0xFFF5F5F5)

    val Neutral150 = Color(0xFFF0F0F0)

    val Neutral200 = Color(0xFFE5E5E5)

    val Neutral300 = Color(0xFFD4D4D4)

    val Neutral400 = Color(0xFFA3A3A3)

    val Neutral500 = Color(0xFF737373)

    val Neutral600 = Color(0xFF525252)

    val Neutral700 = Color(0xFF404040)

    val Neutral800 = Color(0xFF262626)

    val Neutral850 = Color(0xFF1F1F1F)

    val Neutral900 = Color(0xFF171717)

    val Neutral950 = Color(0xFF0F0F0F)

    val Neutral1000 = Color(0xFF080808)


    // ============================================================
    // ELYRA ACCENT
    //
    // Restrained blue-violet accent.
    //
    // This should NOT dominate the interface.
    // It is primarily used for actions, focus and emphasis.
    // ============================================================

    val Accent50 = Color(0xFFF5F3FF)

    val Accent100 = Color(0xFFEDE9FE)

    val Accent200 = Color(0xFFDDD6FE)

    val Accent300 = Color(0xFFC4B5FD)

    val Accent400 = Color(0xFFA78BFA)

    val Accent500 = Color(0xFF8B5CF6)

    val Accent600 = Color(0xFF7C3AED)

    val Accent700 = Color(0xFF6D28D9)

    val Accent800 = Color(0xFF5B21B6)

    val Accent900 = Color(0xFF4C1D95)


    // ============================================================
    // BLUE
    // ============================================================

    val Blue100 = Color(0xFFDBEAFE)

    val Blue500 = Color(0xFF3B82F6)

    val Blue600 = Color(0xFF2563EB)

    val Blue900 = Color(0xFF1E3A8A)


    // ============================================================
    // CYAN
    // ============================================================

    val Cyan100 = Color(0xFFCFFAFE)

    val Cyan400 = Color(0xFF22D3EE)

    val Cyan600 = Color(0xFF0891B2)

    val Cyan800 = Color(0xFF155E75)

    val Cyan900 = Color(0xFF164E63)


    // ============================================================
    // GREEN
    // ============================================================

    val Green100 = Color(0xFFDCFCE7)

    val Green500 = Color(0xFF22C55E)

    val Green600 = Color(0xFF16A34A)

    val Green900 = Color(0xFF14532D)


    // ============================================================
    // AMBER
    // ============================================================

    val Amber100 = Color(0xFFFEF3C7)

    val Amber500 = Color(0xFFF59E0B)

    val Amber600 = Color(0xFFD97706)

    val Amber900 = Color(0xFF78350F)


    // ============================================================
    // RED
    // ============================================================

    val Red100 = Color(0xFFFEE2E2)

    val Red500 = Color(0xFFEF4444)

    val Red600 = Color(0xFFDC2626)

    val Red900 = Color(0xFF7F1D1D)


    // ============================================================
    // SPECIAL
    // ============================================================

    /**
     * Used behind modal overlays.
     */
    val Scrim = Color(0x99000000)
}