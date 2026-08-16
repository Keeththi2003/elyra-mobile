package com.keeththigan.elyra.feature.settings.about

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .padding(horizontal = 20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
                    .clickable(
                        onClick = onBack
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(21.dp),
                    tint = ElyraTheme.colors.textPrimary
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "About Elyra",
                style = ElyraTheme.typography.titleLarge,
                color = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .size(76.dp)
                    .clip(
                        RoundedCornerShape(22.dp)
                    )
                    .background(
                        ElyraTheme.colors.primary
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "E",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.onPrimary
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Elyra",
                style = ElyraTheme.typography.headlineMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Smart living, simplified.",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Version 1.0.0",
                style = ElyraTheme.typography.labelMedium,
                color = ElyraTheme.colors.textTertiary
            )
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        AboutItem(
            icon = Icons.Outlined.Description,
            title = "Terms of Service",
            onClick = { /* TODO: open the hosted document */ }
        )

        AboutDivider()

        AboutItem(
            icon = Icons.Outlined.PrivacyTip,
            title = "Privacy Policy",
            onClick = { /* TODO: open the hosted document */ }
        )

        AboutDivider()

        AboutItem(
            icon = Icons.Outlined.Security,
            title = "Open source licenses",
            onClick = { /* TODO: open the hosted document */ }
        )
    }
}

@Composable
private fun AboutItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                vertical = 16.dp,
                horizontal = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    RoundedCornerShape(13.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}

@Composable
private fun AboutDivider() {

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                ElyraTheme.colors.borderSubtle
            )
    )
}
