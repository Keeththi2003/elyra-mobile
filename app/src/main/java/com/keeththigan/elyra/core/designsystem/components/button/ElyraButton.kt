package com.keeththigan.elyra.core.designsystem.components.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.dimensions.ElyraDimensions
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing

/**
 * Elyra primary button.
 *
 * Use for the main action of a screen.
 *
 * Example:
 *
 * ElyraButton(
 *     text = "Continue",
 *     onClick = { }
 * )
 */
@Composable
fun ElyraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false
) {
    val buttonModifier =
        if (fullWidth) {
            modifier.fillMaxWidth()
        } else {
            modifier
        }

    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = buttonModifier
            .defaultMinSize(
                minHeight = ElyraDimensions.buttonHeight
            ),
        shape = ElyraShapes.button,
        contentPadding = PaddingValues(
            horizontal = ElyraSpacing.buttonHorizontal,
            vertical = ElyraSpacing.buttonVertical
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.defaultMinSize(
                    minWidth = 20.dp,
                    minHeight = 20.dp
                ),
                color = ElyraTheme.colors.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = ElyraTheme.typography.labelLarge
            )
        }
    }
}


/**
 * Elyra secondary button.
 *
 * Use when the action is important but not the
 * primary action of the screen.
 */
@Composable
fun ElyraSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false
) {
    val buttonModifier =
        if (fullWidth) {
            modifier.fillMaxWidth()
        } else {
            modifier
        }

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = buttonModifier
            .defaultMinSize(
                minHeight = ElyraDimensions.buttonHeight
            ),
        shape = ElyraShapes.button,
        contentPadding = PaddingValues(
            horizontal = ElyraSpacing.buttonHorizontal,
            vertical = ElyraSpacing.buttonVertical
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.defaultMinSize(
                    minWidth = 20.dp,
                    minHeight = 20.dp
                ),
                color = ElyraTheme.colors.primary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                style = ElyraTheme.typography.labelLarge
            )
        }
    }
}