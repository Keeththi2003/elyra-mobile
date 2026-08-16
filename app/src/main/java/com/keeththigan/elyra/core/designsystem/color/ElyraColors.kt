package com.keeththigan.elyra.core.designsystem.color

import androidx.compose.ui.graphics.Color

/**
 * Elyra Primitive Color Tokens
 *
 * Monochrome-first design system inspired by modern
 * products such as Apple, X and Uber.
 *
 * IMPORTANT:
 * Feature screens should NOT use these colors directly.
 * Use ElyraTheme.colors instead.
 *
 * Architecture:
 *
 * Primitive Colors
 *       ↓
 * Semantic Colors
 *       ↓
 * Components
 *       ↓
 * Features / Screens
 */
object ElyraColors {

    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)

    val Neutral50 = Color(0xFFFAFAFA)

    val Neutral100 = Color(0xFFF7F7F7)

    val Neutral150 = Color(0xFFF2F2F2)

    val Neutral200 = Color(0xFFE5E5E5)

    val Neutral300 = Color(0xFFD6D6D6)

    val Neutral400 = Color(0xFFA8A8A8)

    val Neutral500 = Color(0xFF737373)

    val Neutral600 = Color(0xFF5F6368)

    val Neutral700 = Color(0xFF404040)

    val Neutral800 = Color(0xFF2A2A2A)

    val Neutral850 = Color(0xFF222222)

    val Neutral900 = Color(0xFF171717)

    val Neutral950 = Color(0xFF0A0A0A)

    val Neutral1000 = Color(0xFF000000)

    val Green100 = Color(0xFFEAF7EE)
    val Green500 = Color(0xFF34A853)
    val Green600 = Color(0xFF188038)
    val Green900 = Color(0xFF0B4627)

    val Amber100 = Color(0xFFFFF4D6)
    val Amber500 = Color(0xFFF5A623)
    val Amber600 = Color(0xFFB7791F)
    val Amber900 = Color(0xFF633C00)

    val Red100 = Color(0xFFFFEBEB)
    val Red500 = Color(0xFFFF453A)
    val Red600 = Color(0xFFD70015)
    val Red900 = Color(0xFF8B0000)

    val Blue100 = Color(0xFFEAF3FF)
    val Blue500 = Color(0xFF0A84FF)
    val Blue600 = Color(0xFF0066CC)
    val Blue900 = Color(0xFF003A75)

    val Scrim = Color(0x99000000)

    val Transparent = Color.Transparent
}
