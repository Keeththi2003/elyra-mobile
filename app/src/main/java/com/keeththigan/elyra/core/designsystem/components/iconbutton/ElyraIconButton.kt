package com.keeththigan.elyra.core.designsystem.components.iconbutton

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.dimensions.ElyraDimensions

/**
 * Standard Elyra icon button.
 *
 * The visual icon may be small, but the interactive area
 * remains large enough for comfortable touch interaction.
 */
@Composable
fun ElyraIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(
            ElyraDimensions.iconButtonSize
        ),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = ElyraTheme.colors.textPrimary,
            disabledContentColor = ElyraTheme.colors.textDisabled
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(
                ElyraDimensions.iconMedium
            )
        )
    }
}