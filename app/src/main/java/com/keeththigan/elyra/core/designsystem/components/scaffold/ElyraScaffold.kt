package com.keeththigan.elyra.core.designsystem.components.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.keeththigan.elyra.core.designsystem.ElyraTheme

/**
 * Standard screen scaffold for Elyra.
 *
 * Provides a consistent application background and
 * Material 3 scaffold structure.
 *
 * Screen-specific navigation and UI are supplied
 * by the feature layer.
 */
@Composable
fun ElyraScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background),

        containerColor = ElyraTheme.colors.background,

        topBar = topBar,

        bottomBar = bottomBar,

        floatingActionButton = floatingActionButton,

        content = content
    )
}