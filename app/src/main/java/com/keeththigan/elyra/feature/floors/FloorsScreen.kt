package com.keeththigan.elyra.feature.floors

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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Search
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
// FLOOR MODEL
// ============================================================================

private data class ElyraFloor(
    val id: String,
    val name: String,
    val roomCount: Int,
    val deviceCount: Int,
    val activeDeviceCount: Int
)


// ============================================================================
// SAMPLE DATA
// ============================================================================

private val sampleFloors = listOf(

    ElyraFloor(
        id = "ground",
        name = "Ground Floor",
        roomCount = 3,
        deviceCount = 5,
        activeDeviceCount = 3
    ),

    ElyraFloor(
        id = "first",
        name = "First Floor",
        roomCount = 2,
        deviceCount = 4,
        activeDeviceCount = 1
    )
)


// ============================================================================
// FLOORS SCREEN
// ============================================================================

@Composable
fun FloorsScreen(
    onFloorClick: (String) -> Unit,
    onAddFloor: () -> Unit,
    onDeleteFloor: (String) -> Unit = {}
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredFloors =
        sampleFloors.filter { floor ->

            searchQuery.isBlank() ||
                    floor.name.contains(
                        searchQuery,
                        ignoreCase = true
                    )
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
                    text = "Floors",
                    style = ElyraTheme.typography.displaySmall,
                    color = ElyraTheme.colors.textPrimary
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "${sampleFloors.size} floors · Manage your home",
                    style = ElyraTheme.typography.bodyMedium,
                    color = ElyraTheme.colors.textSecondary
                )
            }


            // ================================================================
            // ADD FLOOR
            // ================================================================

            IconButton(
                onClick = onAddFloor,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.primary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add floor",
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

        FloorSearchField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            }
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )


        // ====================================================================
        // SECTION TITLE
        // ====================================================================

        Text(
            text = "Your floors",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ====================================================================
        // FLOOR LIST
        // ====================================================================

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = filteredFloors,
                key = {
                    it.id
                }
            ) { floor ->

                FloorCard(
                    floor = floor,
                    onClick = {
                        onFloorClick(floor.id)
                    },
                    onDelete = {
                        onDeleteFloor(floor.id)
                    }
                )
            }


            // ================================================================
            // EMPTY STATE
            // ================================================================

            if (filteredFloors.isEmpty()) {

                item {

                    EmptyFloorsState()
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
private fun FloorSearchField(
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
            contentDescription = "Search floors",
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
                    text = "Search floors",
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
                keyboardOptions =
                    androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = ImeAction.Search
                    )
            )
        }
    }
}


// ============================================================================
// FLOOR CARD
// ============================================================================

@Composable
private fun FloorCard(
    floor: ElyraFloor,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                ElyraTheme.colors.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 17.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ====================================================================
        // FLOOR ICON
        // ====================================================================

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(
                    RoundedCornerShape(17.dp)
                )
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(27.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )


        // ====================================================================
        // FLOOR INFORMATION
        // ====================================================================

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = floor.name,
                style = ElyraTheme.typography.titleMedium,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text =
                    "${floor.roomCount} rooms · ${floor.deviceCount} devices",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ================================================================
            // ACTIVE DEVICE STATUS
            // ================================================================

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFF22C55E)
                        )
                )

                Spacer(
                    modifier = Modifier.size(6.dp)
                )

                Text(
                    text =
                        "${floor.activeDeviceCount} active",
                    style = ElyraTheme.typography.labelMedium,
                    color = Color(0xFF22C55E)
                )
            }
        }


        


        // ====================================================================
        // OPEN
        // ====================================================================

        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = "Open ${floor.name}",
            modifier = Modifier.size(16.dp),
            tint = ElyraTheme.colors.textTertiary
        )
    }
}


// ============================================================================
// EMPTY STATE
// ============================================================================

@Composable
private fun EmptyFloorsState() {

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
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    ElyraTheme.colors.surface
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Apartment,
                contentDescription = null,
                modifier = Modifier.size(27.dp),
                tint = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "No floors found",
            style = ElyraTheme.typography.titleMedium,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Try another search.",
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textSecondary
        )
    }
}