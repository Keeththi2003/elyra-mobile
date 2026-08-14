package com.keeththigan.elyra.feature.devices

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme

// ============================================================================
// DEVICE TYPE
// ============================================================================

private enum class DeviceType {
    LIGHT,
    OUTLET,
    MULTI_SWITCH,
    IRON,
    CAMERA,
    AC
}


// ============================================================================
// DEVICE STATUS
// ============================================================================

private enum class DeviceStatus {
    ON,
    OFF,
    ERROR,
    DISCONNECTED
}


// ============================================================================
// DEVICE MODEL
// ============================================================================

private data class ElyraDevice(
    val id: String,
    val name: String,
    val floorName: String,
    val roomName: String,
    val type: DeviceType,
    val status: DeviceStatus
)


// ============================================================================
// SAMPLE DEVICES
// ============================================================================

private val sampleDevices = listOf(

    ElyraDevice(
        id = "living_room_light",
        name = "Living Room Light",
        floorName = "Ground Floor",
        roomName = "Living Room",
        type = DeviceType.LIGHT,
        status = DeviceStatus.ON
    ),

    ElyraDevice(
        id = "kitchen_outlet",
        name = "Kitchen Outlet",
        floorName = "Ground Floor",
        roomName = "Kitchen",
        type = DeviceType.OUTLET,
        status = DeviceStatus.OFF
    ),

    ElyraDevice(
        id = "kitchen_switch",
        name = "Kitchen Switch",
        floorName = "Ground Floor",
        roomName = "Kitchen",
        type = DeviceType.MULTI_SWITCH,
        status = DeviceStatus.ON
    ),

    ElyraDevice(
        id = "entrance_camera",
        name = "Entrance Camera",
        floorName = "Ground Floor",
        roomName = "Entrance",
        type = DeviceType.CAMERA,
        status = DeviceStatus.ON
    ),

    ElyraDevice(
        id = "living_room_ac",
        name = "Living Room AC",
        floorName = "Ground Floor",
        roomName = "Living Room",
        type = DeviceType.AC,
        status = DeviceStatus.OFF
    ),

    ElyraDevice(
        id = "bedroom_light",
        name = "Bedroom Light",
        floorName = "First Floor",
        roomName = "Bedroom",
        type = DeviceType.LIGHT,
        status = DeviceStatus.DISCONNECTED
    ),

    ElyraDevice(
        id = "bedroom_ac",
        name = "Bedroom AC",
        floorName = "First Floor",
        roomName = "Bedroom",
        type = DeviceType.AC,
        status = DeviceStatus.OFF
    ),

    ElyraDevice(
        id = "bedroom_iron",
        name = "Bedroom Iron",
        floorName = "First Floor",
        roomName = "Bedroom",
        type = DeviceType.IRON,
        status = DeviceStatus.OFF
    ),

    ElyraDevice(
        id = "office_light",
        name = "Office Light",
        floorName = "First Floor",
        roomName = "Office",
        type = DeviceType.LIGHT,
        status = DeviceStatus.ERROR
    )
)


// ============================================================================
// DEVICES SCREEN
// ============================================================================

@Composable
fun DevicesScreen(
    onDeviceClick: (String) -> Unit,
    onAddDevice: () -> Unit = {}
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(DeviceFilter.ALL)
    }

    val filteredDevices = sampleDevices.filter { device ->

        val matchesSearch =
            searchQuery.isBlank() ||
                    device.name.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    device.roomName.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                    device.floorName.contains(
                        searchQuery,
                        ignoreCase = true
                    )

        val matchesFilter =
            when (selectedFilter) {

                DeviceFilter.ALL ->
                    true

                DeviceFilter.ON ->
                    device.status == DeviceStatus.ON

                DeviceFilter.OFF ->
                    device.status == DeviceStatus.OFF

                DeviceFilter.ISSUES ->
                    device.status == DeviceStatus.ERROR ||
                            device.status == DeviceStatus.DISCONNECTED
            }

        matchesSearch && matchesFilter
    }

    val onlineCount =
        sampleDevices.count {
            it.status == DeviceStatus.ON
        }

    val issueCount =
        sampleDevices.count {
            it.status == DeviceStatus.ERROR ||
                    it.status == DeviceStatus.DISCONNECTED
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                ElyraTheme.colors.background
            )
            .padding(
                horizontal = 20.dp
            )
    ) {

        // ====================================================================
        // HEADER
        // ====================================================================

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Devices",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "${sampleDevices.size} devices · $onlineCount active",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )
            }

            // ================================================================
            // ADD DEVICE
            // ================================================================

            IconButton(
                onClick = onAddDevice,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.primary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add device",
                    tint = ElyraTheme.colors.onPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ====================================================================
        // SEARCH
        // ====================================================================

        DeviceSearchField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // ====================================================================
        // FILTERS
        // ====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DeviceFilterChip(
                title = "All",
                count = sampleDevices.size,
                selected = selectedFilter == DeviceFilter.ALL,
                onClick = {
                    selectedFilter = DeviceFilter.ALL
                }
            )

            DeviceFilterChip(
                title = "On",
                count = onlineCount,
                selected = selectedFilter == DeviceFilter.ON,
                onClick = {
                    selectedFilter = DeviceFilter.ON
                }
            )

            DeviceFilterChip(
                title = "Off",
                count = sampleDevices.count {
                    it.status == DeviceStatus.OFF
                },
                selected = selectedFilter == DeviceFilter.OFF,
                onClick = {
                    selectedFilter = DeviceFilter.OFF
                }
            )

            DeviceFilterChip(
                title = "Issues",
                count = issueCount,
                selected = selectedFilter == DeviceFilter.ISSUES,
                onClick = {
                    selectedFilter = DeviceFilter.ISSUES
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ====================================================================
        // DEVICE LIST
        // ====================================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {

                Text(
                    text = "All devices",
                    style = ElyraTheme.typography.titleMedium,
                    color = ElyraTheme.colors.textPrimary,
                    modifier = Modifier.padding(
                        horizontal = 2.dp
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            items(
                items = filteredDevices,
                key = {
                    it.id
                }
            ) { device ->

                DeviceCard(
                    device = device,
                    onClick = {
                        onDeviceClick(device.id)
                    }
                )
            }

            if (filteredDevices.isEmpty()) {

                item {

                    EmptyDevicesState()
                }
            }

            item {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}


// ============================================================================
// SEARCH FIELD
// ============================================================================

@Composable
private fun DeviceSearchField(
    value: String,
    onValueChange: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .padding(
                horizontal = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search",
            modifier = Modifier.size(21.dp),
            tint = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.size(10.dp)
        )

        Box(
            modifier = Modifier.weight(1f)
        ) {

            if (value.isEmpty()) {

                Text(
                    text = "Search devices",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textTertiary
                )
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = ElyraTheme.typography.bodyMedium.copy(
                    color = ElyraTheme.colors.textPrimary
                ),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )
        }
    }
}


// ============================================================================
// FILTER
// ============================================================================

private enum class DeviceFilter {
    ALL,
    ON,
    OFF,
    ISSUES
}


// ============================================================================
// FILTER CHIP
// ============================================================================

@Composable
private fun DeviceFilterChip(
    title: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {

    val background =
        if (selected) {
            ElyraTheme.colors.primary
        } else {
            ElyraTheme.colors.surface
        }

    val textColor =
        if (selected) {
            ElyraTheme.colors.onPrimary
        } else {
            ElyraTheme.colors.textSecondary
        }

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .background(background)
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 13.dp,
                vertical = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            style = ElyraTheme.typography.labelMedium,
            color = textColor
        )

        Spacer(
            modifier = Modifier.size(5.dp)
        )

        Text(
            text = count.toString(),
            style = ElyraTheme.typography.labelMedium,
            color = textColor
        )
    }
}


// ============================================================================
// DEVICE CARD
// ============================================================================

@Composable
private fun DeviceCard(
    device: ElyraDevice,
    onClick: () -> Unit
) {

    val statusColor =
        when (device.status) {

            DeviceStatus.ON ->
                Color(0xFF22C55E)

            DeviceStatus.OFF ->
                ElyraTheme.colors.textTertiary

            DeviceStatus.ERROR ->
                Color(0xFFEF4444)

            DeviceStatus.DISCONNECTED ->
                Color(0xFFF59E0B)
        }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ====================================================================
        // DEVICE ICON
        // ====================================================================

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .background(
                    statusColor.copy(
                        alpha = 0.10f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = getDeviceIcon(device.type),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = statusColor
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )


        // ====================================================================
        // INFORMATION
        // ====================================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = device.name,
                style = ElyraTheme.typography.titleSmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "${device.floorName} · ${device.roomName}",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text = getStatusText(device.status),
                    style = ElyraTheme.typography.labelMedium,
                    color = statusColor
                )
            }
        }


        // ====================================================================
        // ARROW
        // ====================================================================

        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = "Open ${device.name}",
            modifier = Modifier.size(16.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// EMPTY STATE
// ============================================================================

@Composable
private fun EmptyDevicesState() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 60.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.surface
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "No devices found",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Try a different search or filter.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}


// ============================================================================
// DEVICE ICON
// ============================================================================

private fun getDeviceIcon(
    type: DeviceType
): ImageVector {

    return when (type) {

        DeviceType.LIGHT ->
            Icons.Outlined.Lightbulb

        DeviceType.OUTLET ->
            Icons.Outlined.Power

        DeviceType.MULTI_SWITCH ->
            Icons.Outlined.ElectricalServices

        DeviceType.IRON ->
            Icons.Outlined.Thermostat

        DeviceType.CAMERA ->
            Icons.Outlined.CameraAlt

        DeviceType.AC ->
            Icons.Outlined.AcUnit
    }
}


// ============================================================================
// STATUS TEXT
// ============================================================================

private fun getStatusText(
    status: DeviceStatus
): String {

    return when (status) {

        DeviceStatus.ON ->
            "On"

        DeviceStatus.OFF ->
            "Off"

        DeviceStatus.ERROR ->
            "Error"

        DeviceStatus.DISCONNECTED ->
            "Disconnected"
    }
}