package com.keeththigan.elyra.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.feature.auth.navigation.AuthNavigation


@Composable
fun ElyraApp() {
    ElyraTheme {
        // Box(
        //     modifier = Modifier.fillMaxSize(),
        //     contentAlignment = Alignment.Center
        // ) {
        //     Text(
        //         text = "Elyra",
        //         color = ElyraTheme.colors.textPrimary,
        //         style = ElyraTheme.typography.headlineLarge
        //     )
        // }
        AuthNavigation()
    }
}