package com.keeththigan.elyra.core.designsystem.components.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.dimensions.ElyraDimensions
import com.keeththigan.elyra.core.designsystem.shape.ElyraShapes
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing

enum class ElyraStatus {
    ONLINE,
    OFFLINE,
    CONNECTING,
    ACTIVE,
    INACTIVE,
    WARNING,
    ERROR
}

@Composable
fun ElyraStatusBadge(
    status: ElyraStatus,
    modifier: Modifier = Modifier
) {
    val (label, color, containerColor) = when (status) {

        ElyraStatus.ONLINE -> Triple(
            "Online",
            ElyraTheme.colors.success,
            ElyraTheme.colors.successContainer
        )

        ElyraStatus.OFFLINE -> Triple(
            "Offline",
            ElyraTheme.colors.deviceOffline,
            ElyraTheme.colors.errorContainer
        )

        ElyraStatus.CONNECTING -> Triple(
            "Connecting",
            ElyraTheme.colors.warning,
            ElyraTheme.colors.warningContainer
        )

        ElyraStatus.ACTIVE -> Triple(
            "Active",
            ElyraTheme.colors.success,
            ElyraTheme.colors.successContainer
        )

        ElyraStatus.INACTIVE -> Triple(
            "Inactive",
            ElyraTheme.colors.textSecondary,
            ElyraTheme.colors.surfaceSecondary
        )

        ElyraStatus.WARNING -> Triple(
            "Warning",
            ElyraTheme.colors.warning,
            ElyraTheme.colors.warningContainer
        )

        ElyraStatus.ERROR -> Triple(
            "Error",
            ElyraTheme.colors.error,
            ElyraTheme.colors.errorContainer
        )
    }

    Row(
        modifier = modifier
            .clip(ElyraShapes.pill)
            .background(containerColor)
            .padding(
                horizontal = ElyraSpacing.sm,
                vertical = ElyraSpacing.xs
            ),
        horizontalArrangement = Arrangement.spacedBy(
            ElyraSpacing.xs
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(ElyraDimensions.statusIndicator)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = label,
            style = ElyraTheme.typography.labelMedium,
            color = ElyraTheme.colors.textPrimary
        )
    }
}