package com.keeththigan.elyra.feature.settings.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.MeetingRoom
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
import com.keeththigan.elyra.feature.auth.AuthViewModel
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.floors.FloorViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    deviceViewModel: DeviceViewModel,
    floorViewModel: FloorViewModel,
    roomViewModel: RoomViewModel,
    onBack: () -> Unit
) {

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()
    val floorState by floorViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()

    val user = authState.user

    val name = user?.name.orEmpty().ifBlank { "Your account" }
    val email = user?.email.orEmpty()

    val memberSince =
        user?.createdAt?.toDate()?.let {
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(it)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        ElyraDetailTopBar(
            title = "Profile",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(ElyraTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = name.take(1).uppercase(),
                        style = ElyraTheme.typography.displaySmall,
                        color = ElyraTheme.colors.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = name,
                    style = ElyraTheme.typography.headlineSmall,
                    color = ElyraTheme.colors.textPrimary
                )

                if (email.isNotBlank()) {

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email,
                        style = ElyraTheme.typography.bodyMedium,
                        color = ElyraTheme.colors.textSecondary
                    )
                }

                if (memberSince != null) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ElyraTheme.colors.surfaceSecondary)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {

                        Text(
                            text = "Member since $memberSince",
                            style = ElyraTheme.typography.labelMedium,
                            color = ElyraTheme.colors.textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Your home",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                StatTile(
                    icon = Icons.Outlined.Apartment,
                    value = floorState.floors.size.toString(),
                    label = "Floors",
                    modifier = Modifier.weight(1f)
                )

                StatTile(
                    icon = Icons.Outlined.MeetingRoom,
                    value = roomState.rooms.size.toString(),
                    label = "Rooms",
                    modifier = Modifier.weight(1f)
                )

                StatTile(
                    icon = Icons.Outlined.Devices,
                    value = deviceState.devices.size.toString(),
                    label = "Devices",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Account",
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

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
            ) {

                DetailRow(label = "Name", value = name)

                RowDivider()

                DetailRow(
                    label = "Email",
                    value = email.ifBlank { "—" }
                )

                RowDivider()

                DetailRow(
                    label = "User ID",
                    value = user?.id.orEmpty().ifBlank { "—" }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = ElyraTheme.colors.borderSubtle,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { authViewModel.signOut() }
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = ElyraTheme.colors.error
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Sign out",
                    style = ElyraTheme.typography.labelLarge,
                    color = ElyraTheme.colors.error
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatTile(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(vertical = 16.dp, horizontal = 14.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = value,
            style = ElyraTheme.typography.headlineSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value,
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun RowDivider() {

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(ElyraTheme.colors.borderSubtle)
    )
}
