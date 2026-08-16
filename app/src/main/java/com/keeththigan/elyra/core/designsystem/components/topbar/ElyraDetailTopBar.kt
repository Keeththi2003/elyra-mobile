package com.keeththigan.elyra.core.designsystem.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

/**
 * Shared header for pushed screens: back affordance, title, optional
 * subtitle and trailing actions.
 *
 * Every detail screen uses this so titles and back buttons stay in the
 * same place and at the same weight across the app.
 */
@Composable
fun ElyraDetailTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (onBack != null) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(ElyraTheme.colors.surface)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(19.dp),
                    tint = ElyraTheme.colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = title,
                style = ElyraTheme.typography.titleLarge,
                color = ElyraTheme.colors.textPrimary,
                maxLines = 1
            )

            if (!subtitle.isNullOrBlank()) {

                Text(
                    text = subtitle,
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary,
                    maxLines = 1
                )
            }
        }

        actions()
    }
}
