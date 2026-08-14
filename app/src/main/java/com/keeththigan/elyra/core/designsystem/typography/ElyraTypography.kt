package com.keeththigan.elyra.core.designsystem.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.keeththigan.elyra.R

/**
 * Elyra Typography System
 *
 * Typography hierarchy:
 *
 * Display
 *   ↓
 * Headlines
 *   ↓
 * Titles
 *   ↓
 * Body
 *   ↓
 * Labels / Captions
 *
 * The system is intentionally restrained.
 * Most application content should use Body, Title,
 * and Headline styles rather than oversized typography.
 */

// ================================================================
// FONT FAMILY
// ================================================================

val ElyraFontFamily = FontFamily(
    Font(
        resId = R.font.inter_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.inter_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.inter_bold,
        weight = FontWeight.Bold
    )
)


// ================================================================
// TYPOGRAPHY
// ================================================================

val ElyraTypography = Typography(

    // ------------------------------------------------------------
    // DISPLAY
    // ------------------------------------------------------------

    displayLarge = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.5).sp
    ),

    displayMedium = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.4).sp
    ),

    displaySmall = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.3).sp
    ),


    // ------------------------------------------------------------
    // HEADLINES
    // ------------------------------------------------------------

    headlineLarge = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.25).sp
    ),

    headlineMedium = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp
    ),

    headlineSmall = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),


    // ------------------------------------------------------------
    // TITLES
    // ------------------------------------------------------------

    titleLarge = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),

    titleMedium = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),

    titleSmall = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),


    // ------------------------------------------------------------
    // BODY
    // ------------------------------------------------------------

    bodyLarge = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp
    ),

    bodySmall = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.05.sp
    ),


    // ------------------------------------------------------------
    // LABELS
    // ------------------------------------------------------------

    labelLarge = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp
    ),

    labelMedium = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ),

    labelSmall = TextStyle(
        fontFamily = ElyraFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.2.sp
    )
)