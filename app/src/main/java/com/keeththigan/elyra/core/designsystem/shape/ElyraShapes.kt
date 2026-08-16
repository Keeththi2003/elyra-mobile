package com.keeththigan.elyra.core.designsystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Corner radii for the app. Feature screens should not define their own. */
object ElyraShapes {

    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp

    /** Large enough to always render as a semicircle at any height. */
    val full = 999.dp

    val card = RoundedCornerShape(lg)
    val featureCard = RoundedCornerShape(xl)
    val compactCard = RoundedCornerShape(md)
    val input = RoundedCornerShape(md)
    val button = RoundedCornerShape(md)
    val buttonSmall = RoundedCornerShape(sm)
    val pill = RoundedCornerShape(full)
    val dialog = RoundedCornerShape(xxl)

    val bottomSheet = RoundedCornerShape(
        topStart = xxl,
        topEnd = xxl,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    /** Applied through the theme so Material components inherit these radii. */
    val material = Shapes(
        extraSmall = RoundedCornerShape(xs),
        small = RoundedCornerShape(sm),
        medium = RoundedCornerShape(md),
        large = RoundedCornerShape(lg),
        extraLarge = RoundedCornerShape(xxl)
    )
}
