package com.keeththigan.elyra.feature.reports

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.topbar.ElyraDetailTopBar
import com.keeththigan.elyra.data.model.Device
import com.keeththigan.elyra.data.model.DeviceStatus
import com.keeththigan.elyra.data.model.DeviceType
import com.keeththigan.elyra.feature.devices.DeviceViewModel
import com.keeththigan.elyra.feature.floors.RoomViewModel

/**
 * Usage reporting across every device.
 *
 * Runtime is accumulated on each device document as it is switched off, so
 * these figures come straight from Firestore and stay correct across
 * devices and reinstalls.
 */
@Composable
fun ReportsScreen(
    deviceViewModel: DeviceViewModel,
    roomViewModel: RoomViewModel,
    onDeviceClick: (String) -> Unit,
    onBack: () -> Unit,
    /** When set, the report covers only this device. */
    deviceId: String = ""
) {

    val deviceState by deviceViewModel.state.collectAsStateWithLifecycle()
    val roomState by roomViewModel.state.collectAsStateWithLifecycle()

    val singleDevice =
        deviceId.takeIf { it.isNotBlank() }
            ?.let { id -> deviceState.devices.find { it.id == id } }

    // Scope every figure below to one device when we arrived from its detail
    // screen, otherwise report across the whole home.
    val devices =
        if (singleDevice != null) listOf(singleDevice) else deviceState.devices

    val totalRuntime = devices.sumOf { it.totalOnSeconds }
    val activeNow = devices.count { it.status == DeviceStatus.ON }

    val ranked =
        devices.sortedByDescending { it.totalOnSeconds }
            .filter { it.totalOnSeconds > 0 }

    val byType =
        DeviceType.entries.map { type ->
            type to devices.filter { it.type == type }
                .sumOf { it.totalOnSeconds }
        }.filter { it.second > 0 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
    ) {

        ElyraDetailTopBar(
            title = singleDevice?.let { "${it.name} report" } ?: "Reports",
            subtitle = singleDevice?.let { "Usage for this device" }
                ?: "Device usage across your home",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ============================================================
            // HEADLINE FIGURES
            // ============================================================

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    MetricTile(
                        value = formatDuration(totalRuntime),
                        label = "Total runtime",
                        modifier = Modifier.weight(1f)
                    )

                    MetricTile(
                        value = if (singleDevice != null) {
                            formatDuration(
                                devices.firstOrNull()?.totalOnSeconds ?: 0L
                            )
                        } else {
                            devices.size.toString()
                        },
                        label = if (singleDevice != null) {
                            "Recorded runtime"
                        } else {
                            "Devices tracked"
                        },
                        modifier = Modifier.weight(1f)
                    )

                    MetricTile(
                        value = activeNow.toString(),
                        label = "Running now",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ============================================================
            // RUNTIME BY DEVICE
            // ============================================================

            item {
                SectionTitle(
                    title = if (singleDevice != null) {
                        "Runtime"
                    } else {
                        "Runtime by device"
                    },
                    subtitle = if (singleDevice != null) {
                        "Total recorded for this device"
                    } else {
                        "Longest running first"
                    }
                )
            }

            if (ranked.isEmpty()) {

                item {
                    EmptyReport(
                        message = "No runtime recorded yet. Switch a device " +
                            "on and off to start collecting usage."
                    )
                }

            } else {

                val maxSeconds = ranked.first().totalOnSeconds.coerceAtLeast(1)

                items(
                    items = ranked,
                    key = { it.id }
                ) { device ->

                    UsageBar(
                        label = device.name,
                        sublabel = roomState.rooms
                            .find { it.id == device.roomId }
                            ?.name
                            ?: "Unassigned",
                        seconds = device.totalOnSeconds,
                        fraction = device.totalOnSeconds.toFloat() /
                            maxSeconds.toFloat(),
                        onClick = { onDeviceClick(device.id) }
                    )
                }
            }

            // ============================================================
            // SHARE OF RUNTIME BY TYPE
            // ============================================================

            if (byType.isNotEmpty() && singleDevice == null) {

                item {
                    SectionTitle(
                        title = "Share by device type",
                        subtitle = "Where your energy time goes"
                    )
                }

                item {
                    TypeBreakdown(
                        data = byType,
                        total = byType.sumOf { it.second }
                    )
                }
            }
        }
    }
}


// ============================================================================
// CHARTS
// ============================================================================

@Composable
private fun UsageBar(
    label: String,
    sublabel: String,
    seconds: Long,
    fraction: Float,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = label,
                    style = ElyraTheme.typography.bodyLarge,
                    color = ElyraTheme.colors.textPrimary,
                    maxLines = 1
                )

                Text(
                    text = sublabel,
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary,
                    maxLines = 1
                )
            }

            Text(
                text = formatDuration(seconds),
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val trackColor = ElyraTheme.colors.surfaceInteractive
        val fillColor = ElyraTheme.colors.primary

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        ) {

            drawRoundRect(
                color = trackColor,
                cornerRadius = androidx.compose.ui.geometry
                    .CornerRadius(size.height / 2)
            )

            drawRoundRect(
                color = fillColor,
                size = Size(
                    width = size.width * fraction.coerceIn(0f, 1f),
                    height = size.height
                ),
                cornerRadius = androidx.compose.ui.geometry
                    .CornerRadius(size.height / 2)
            )
        }
    }
}


@Composable
private fun TypeBreakdown(
    data: List<Pair<DeviceType, Long>>,
    total: Long
) {

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
            .padding(18.dp)
    ) {

        // Monochrome ramp keeps the chart on-brand and readable in both themes.
        val shades = listOf(1f, 0.78f, 0.58f, 0.4f, 0.26f)
        val base = ElyraTheme.colors.textPrimary
        val track = ElyraTheme.colors.surfaceInteractive

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
        ) {

            drawRoundRect(
                color = track,
                cornerRadius = androidx.compose.ui.geometry
                    .CornerRadius(size.height / 2)
            )

            var startX = 0f

            data.forEachIndexed { index, (_, seconds) ->

                val widthFraction =
                    if (total == 0L) 0f else seconds.toFloat() / total.toFloat()

                val segmentWidth = size.width * widthFraction

                drawRect(
                    color = base.copy(
                        alpha = shades.getOrElse(index) { 0.2f }
                    ),
                    topLeft = Offset(startX, 0f),
                    size = Size(segmentWidth, size.height)
                )

                startX += segmentWidth
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        data.forEachIndexed { index, (type, seconds) ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            base.copy(
                                alpha = shades.getOrElse(index) { 0.2f }
                            )
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = type.label(),
                    modifier = Modifier.weight(1f),
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textPrimary
                )

                Text(
                    text = formatDuration(seconds),
                    style = ElyraTheme.typography.bodySmall,
                    color = ElyraTheme.colors.textSecondary
                )
            }
        }
    }
}


// ============================================================================
// PIECES
// ============================================================================

@Composable
private fun MetricTile(
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
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {

        Text(
            text = value,
            style = ElyraTheme.typography.titleLarge,
            color = ElyraTheme.colors.textPrimary,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary,
            maxLines = 2
        )
    }
}


@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {

    Column {

        Text(
            text = title,
            style = ElyraTheme.typography.titleLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = subtitle,
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


@Composable
private fun EmptyReport(
    message: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ElyraTheme.colors.surface)
            .border(
                width = 1.dp,
                color = ElyraTheme.colors.borderSubtle,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(24.dp)
    ) {

        Text(
            text = message,
            style = ElyraTheme.typography.bodyMedium,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// FORMATTING
// ============================================================================

internal fun formatDuration(
    seconds: Long
): String {

    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "${secs}s"
    }
}


private fun DeviceType.label(): String =
    when (this) {
        DeviceType.LIGHT -> "Lights"
        DeviceType.OUTLET -> "Outlets"
        DeviceType.MULTI_SWITCH -> "Multi-switches"
        DeviceType.SAFETY_APPLIANCE -> "Safety appliances"
        DeviceType.SECURITY_CAMERA -> "Cameras"
    }
