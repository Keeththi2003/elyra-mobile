package com.keeththigan.elyra.feature.auth.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.core.designsystem.components.button.ElyraButton
import com.keeththigan.elyra.core.designsystem.spacing.ElyraSpacing

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = ElyraSpacing.screenHorizontal
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Elyra",
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(
                ElyraSpacing.lg
            )
        )

        Text(
            text = "A smarter way to live.",
            style = ElyraTheme.typography.headlineSmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(
                ElyraSpacing.sm
            )
        )

        Text(
            text = "Connect, control and manage your smart home from one place.",
            style = ElyraTheme.typography.bodyLarge,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(
                ElyraSpacing.xxl
            )
        )

        ElyraButton(
            text = "Get Started",
            onClick = onGetStarted,
            fullWidth = true
        )

        Spacer(
            modifier = Modifier.height(
                ElyraSpacing.md
            )
        )

        ElyraButton(
            text = "I already have an account",
            onClick = onSignIn,
            fullWidth = true
        )
    }
}