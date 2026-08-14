package com.keeththigan.elyra.core.designsystem.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing

/**
 * Standard Elyra card.
 *
 * Used for grouped content and interactive surfaces.
 */
@Composable
fun ElyraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier =
        if (onClick != null) {
            modifier.clickable(onClick = onClick)
        } else {
            modifier
        }

    Card(
        modifier = clickableModifier.fillMaxWidth(),
        shape = ElyraShapes.card,
        colors = CardDefaults.cardColors(
            containerColor = ElyraTheme.colors.surface
        ),
        border = CardDefaults.outlinedCardBorder(
            enabled = true
        )
    ) {
        Column(
            modifier = Modifier.padding(ElyraSpacing.cardPadding),
            content = content
        )
    }
}