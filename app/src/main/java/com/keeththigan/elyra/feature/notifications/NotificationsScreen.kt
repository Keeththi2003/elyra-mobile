package com.keeththigan.elyra.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.topbar.ElyraDetailTopBar
import com.keeththigan.elyra.data.model.AppNotification
import com.keeththigan.elyra.data.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotificationsScreen(
    notificationViewModel: NotificationViewModel,
    onBack: () -> Unit
) {

    val state by notificationViewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        ElyraDetailTopBar(
            title = "Alerts",
            subtitle = if (state.unreadCount > 0) {
                "${state.unreadCount} unread"
            } else {
                "You're all caught up"
            },
            onBack = onBack,
            actions = {
                if (state.notifications.isNotEmpty()) {
                    Text(
                        text = "Clear",
                        modifier = Modifier
                            .clickable { notificationViewModel.clearAll() }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        style = ElyraTheme.typography.labelMedium,
                        color = ElyraTheme.colors.textSecondary
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            if (state.notifications.isEmpty()) {

                item { EmptyAlerts() }

            } else {

                if (state.unreadCount > 0) {

                    item {
                        Text(
                            text = "Mark all as read",
                            modifier = Modifier
                                .clickable { notificationViewModel.markAllAsRead() }
                                .padding(vertical = 6.dp),
                            style = ElyraTheme.typography.labelMedium,
                            color = ElyraTheme.colors.textPrimary
                        )
                    }
                }

                items(
                    items = state.notifications,
                    key = { it.id }
                ) { notification ->

                    NotificationRow(
                        notification = notification,
                        onClick = {
                            if (!notification.isRead) {
                                notificationViewModel.markAsRead(notification.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(
    notification: AppNotification,
    onClick: () -> Unit
) {

    val timestamp =
        notification.createdAt?.toDate()?.let {
            SimpleDateFormat("d MMM · HH:mm", Locale.getDefault()).format(it)
        }.orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (notification.isRead) {
                    ElyraTheme.colors.surface
                } else {
                    ElyraTheme.colors.surfaceSecondary
                }
            )
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ElyraTheme.colors.surfaceInteractive),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = notification.type.icon(),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = notification.title,
                    style = ElyraTheme.typography.titleSmall,
                    color = ElyraTheme.colors.textPrimary
                )

                if (!notification.isRead) {

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(ElyraTheme.colors.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notification.message,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            if (timestamp.isNotBlank()) {

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = timestamp,
                    style = ElyraTheme.typography.labelSmall,
                    color = ElyraTheme.colors.textTertiary
                )
            }
        }
    }
}

@Composable
private fun EmptyAlerts() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ElyraTheme.colors.surfaceSecondary),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.NotificationsNone,
                contentDescription = null,
                modifier = Modifier.size(23.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "No alerts",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Safety cutoffs and device faults will appear here.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}

private fun NotificationType.icon(): ImageVector =
    when (this) {
        NotificationType.SAFETY_CUTOFF -> Icons.Outlined.Security
        NotificationType.DEVICE_ERROR -> Icons.Outlined.WarningAmber
        NotificationType.DEVICE_OFFLINE -> Icons.Outlined.WifiOff
    }
