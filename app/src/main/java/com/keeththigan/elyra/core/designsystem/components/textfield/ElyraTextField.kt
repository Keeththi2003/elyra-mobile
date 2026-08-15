package com.keeththigan.elyra.core.designsystem.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.dimensions.ElyraDimensions
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes

/**
 * Standard Elyra text field.
 *
 * Provides a consistent appearance across the application.
 */
@Composable
fun ElyraTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        modifier = modifier
            .fillMaxWidth()
            .height(ElyraDimensions.inputHeight),

        enabled = enabled,
        singleLine = singleLine,

        shape = ElyraShapes.input,

        label = label?.let {
            {
                Text(
                    text = it,
                    style = ElyraTheme.typography.bodySmall
                )
            }
        },

        placeholder = placeholder?.let {
            {
                Text(
                    text = it,
                    style = ElyraTheme.typography.bodyMedium
                )
            }
        },

        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },

        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = ElyraTheme.typography.bodySmall
                )
            }
        },

        isError = isError,

        visualTransformation = visualTransformation,

        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ElyraTheme.colors.primary,
            unfocusedBorderColor = ElyraTheme.colors.border,
            errorBorderColor = ElyraTheme.colors.error,

            focusedLabelColor = ElyraTheme.colors.primary,
            unfocusedLabelColor = ElyraTheme.colors.textSecondary,

            focusedTextColor = ElyraTheme.colors.textPrimary,
            unfocusedTextColor = ElyraTheme.colors.textPrimary,

            focusedPlaceholderColor = ElyraTheme.colors.textTertiary,
            unfocusedPlaceholderColor = ElyraTheme.colors.textTertiary,

            cursorColor = ElyraTheme.colors.primary,

            disabledTextColor = ElyraTheme.colors.textDisabled,
            disabledBorderColor = ElyraTheme.colors.borderSubtle
        )
    )
}