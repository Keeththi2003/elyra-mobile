package com.keeththigan.elyra.feature.auth.onboarding

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import kotlinx.coroutines.delay


@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit
) {

    // ================================================================
    // SCREEN SIZE
    // ================================================================

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp

    val isSmallScreen = screenHeight < 700

    val isLargeScreen = screenHeight >= 800


    // ================================================================
    // RESPONSIVE VALUES
    // ================================================================

    val horizontalPadding = when {
        isSmallScreen -> 20.dp
        isLargeScreen -> 28.dp
        else -> 24.dp
    }

    val heroSize = when {
        isSmallScreen -> 230.dp
        isLargeScreen -> 310.dp
        else -> 280.dp
    }

    val heroSpacing = when {
        isSmallScreen -> 12.dp
        isLargeScreen -> 24.dp
        else -> 18.dp
    }

    val titleSpacing = if (isSmallScreen) {
        10.dp
    } else {
        14.dp
    }

    val buttonHeight = if (isSmallScreen) {
        50.dp
    } else {
        54.dp
    }


    // ================================================================
    // ENTRANCE ANIMATION
    // ================================================================

    var animationStarted by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(Unit) {
        delay(150)
        animationStarted = 1f
    }

    val heroAlpha by animateFloatAsState(
        targetValue = animationStarted,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "heroAlpha"
    )

    val heroScale by animateFloatAsState(
        targetValue = if (animationStarted == 1f) {
            1f
        } else {
            0.92f
        },
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "heroScale"
    )


    // ================================================================
    // SCREEN
    // ================================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ============================================================
        // BRAND
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
        ) {

            Text(
                text = "ELYRA",
                style = ElyraTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = ElyraTheme.colors.textPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }


        // ============================================================
        // HERO
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = heroSpacing)
                .alpha(heroAlpha)
                .scale(heroScale),
            contentAlignment = Alignment.Center
        ) {

            ElyraHeroGraphic(
                size = heroSize
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )


        // ============================================================
        // TEXT
        // ============================================================

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Your home.",
                style = ElyraTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = ElyraTheme.colors.textPrimary
            )

            Text(
                text = "Your way.",
                style = ElyraTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(titleSpacing)
            )

            Text(
                text = "Control your connected home from one simple, beautifully designed place.",
                style = ElyraTheme.typography.bodyLarge,
                color = ElyraTheme.colors.textSecondary
            )
        }


        Spacer(
            modifier = Modifier.height(
                if (isSmallScreen) 18.dp else 24.dp
            )
        )


        // ============================================================
        // GET STARTED
        // ============================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight)
                .background(
                    color = ElyraTheme.colors.primary,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(onClick = onGetStarted),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Get Started",
                style = ElyraTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = ElyraTheme.colors.onPrimary
            )
        }


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        // ============================================================
        // SIGN IN
        // ============================================================

        Row(
            modifier = Modifier
                .clickable(onClick = onSignIn)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Already have an account?",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Spacer(
                modifier = Modifier.width(5.dp)
            )

            Text(
                text = "Sign in",
                style = ElyraTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = ElyraTheme.colors.textPrimary
            )
        }


        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}


// ============================================================================
// HERO GRAPHIC
// ============================================================================

@Composable
private fun ElyraHeroGraphic(
    size: Dp
) {

    val primary = ElyraTheme.colors.textPrimary

    val secondary = ElyraTheme.colors.textSecondary

    val surface = ElyraTheme.colors.surfaceSecondary


    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {

        // ================================================================
        // OUTER RINGS
        // ================================================================

        Canvas(
            modifier = Modifier.size(size)
        ) {

            drawCircle(
                color = primary.copy(alpha = 0.06f),
                style = Stroke(
                    width = 1.dp.toPx()
                )
            )

            drawCircle(
                color = primary.copy(alpha = 0.035f),
                radius = this.size.minDimension * 0.38f,
                style = Stroke(
                    width = 1.dp.toPx()
                )
            )
        }


        // ================================================================
        // HOME
        // ================================================================

        Box(
            modifier = Modifier
                .size(size * 0.50f)
                .background(
                    color = surface,
                    shape = RoundedCornerShape(size * 0.14f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.size(size * 0.33f)
            ) {

                val canvasWidth = this.size.width
                val canvasHeight = this.size.height


                // --------------------------------------------------------
                // ROOF
                // --------------------------------------------------------

                val roofPath = Path().apply {

                    moveTo(
                        canvasWidth * 0.18f,
                        canvasHeight * 0.45f
                    )

                    lineTo(
                        canvasWidth * 0.50f,
                        canvasHeight * 0.18f
                    )

                    lineTo(
                        canvasWidth * 0.82f,
                        canvasHeight * 0.45f
                    )
                }

                drawPath(
                    path = roofPath,
                    color = primary,
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )


                // --------------------------------------------------------
                // HOUSE BODY
                // --------------------------------------------------------

                drawRoundRect(
                    color = primary,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        canvasWidth * 0.27f,
                        canvasHeight * 0.42f
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        canvasWidth * 0.46f,
                        canvasHeight * 0.34f
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        8.dp.toPx()
                    ),
                    style = Stroke(
                        width = 5.dp.toPx()
                    )
                )


                // --------------------------------------------------------
                // DOOR
                // --------------------------------------------------------

                drawRoundRect(
                    color = primary,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        canvasWidth * 0.44f,
                        canvasHeight * 0.57f
                    ),
                    size = androidx.compose.ui.geometry.Size(
                        canvasWidth * 0.12f,
                        canvasHeight * 0.19f
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        3.dp.toPx()
                    )
                )
            }
        }


        // ================================================================
        // LIGHT
        // ================================================================

        HeroDevice(
            modifier = Modifier.align(Alignment.TopEnd),
            size = size * 0.16f
        ) {

            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = "Smart light",
                tint = primary,
                modifier = Modifier.size(size * 0.065f)
            )
        }


        // ================================================================
        // WIFI
        // ================================================================

        HeroDevice(
            modifier = Modifier.align(Alignment.BottomStart),
            size = size * 0.16f
        ) {

            Icon(
                imageVector = Icons.Outlined.Wifi,
                contentDescription = "Wi-Fi",
                tint = primary,
                modifier = Modifier.size(size * 0.065f)
            )
        }


        // ================================================================
        // LOCK
        // ================================================================

        HeroDevice(
            modifier = Modifier.align(Alignment.BottomEnd),
            size = size * 0.16f
        ) {

            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Smart lock",
                tint = primary,
                modifier = Modifier.size(size * 0.065f)
            )
        }


        // ================================================================
        // CONNECTION DOTS
        // ================================================================

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(size * 0.027f)
                .background(
                    color = secondary.copy(alpha = 0.6f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(size * 0.02f)
                .background(
                    color = secondary.copy(alpha = 0.4f),
                    shape = CircleShape
                )
        )
    }
}


// ============================================================================
// HERO DEVICE
// ============================================================================

@Composable
private fun HeroDevice(
    modifier: Modifier,
    size: Dp,
    icon: @Composable () -> Unit
) {

    Box(
        modifier = modifier
            .size(size)
            .background(
                color = ElyraTheme.colors.surface,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}