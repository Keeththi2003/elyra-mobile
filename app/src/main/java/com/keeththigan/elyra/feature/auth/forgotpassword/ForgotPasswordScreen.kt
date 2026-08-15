package com.keeththigan.elyra.feature.auth.forgotpassword

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keeththigan.elyra.feature.auth.AuthViewModel

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    onSendResetLink: (String) -> Unit,
    onSignIn: () -> Unit,
    authViewModel: AuthViewModel
) {

    var email by remember {
        mutableStateOf("")
    }

    var focused by remember {
        mutableStateOf(false)
    }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val canContinue = email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp)
    ) {

        // ========================================================
        // TOP BAR
        // ========================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp)
        ) {

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(
                        ElyraTheme.colors.surfaceSecondary
                    )
            ) {

                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = ElyraTheme.colors.textPrimary,
                    modifier = Modifier.size(21.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(56.dp)
        )

        // ========================================================
        // ICON
        // ========================================================

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    ElyraTheme.colors.surfaceSecondary
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ========================================================
        // HEADER
        // ========================================================

        Text(
            text = "Forgot your password?",
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Enter the email address connected to your Elyra account. We'll send you a link to reset your password.",
            style = ElyraTheme.typography.bodyLarge,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        // ========================================================
        // EMAIL FIELD
        // ========================================================

        Text(
            text = "Email",
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .onFocusChanged {
                    focused = it.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            textStyle = ElyraTheme.typography.bodyLarge.copy(
                color = ElyraTheme.colors.textPrimary
            ),
            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(14.dp)
                        )
                        .background(
                            ElyraTheme.colors.surface
                        )
                        .border(
                            width = if (focused) 1.5.dp else 1.dp,
                            color =
                                if (focused) {
                                    ElyraTheme.colors.textPrimary
                                } else {
                                    ElyraTheme.colors.border
                                },
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = ElyraTheme.colors.textSecondary
                    )

                    Spacer(
                        modifier = Modifier.size(12.dp)
                    )

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {

                        if (email.isEmpty()) {

                            Text(
                                text = "you@example.com",
                                style = ElyraTheme.typography.bodyLarge,
                                color = ElyraTheme.colors.textTertiary
                            )
                        }

                        innerTextField()
                    }
                }
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // ========================================================
        // SEND BUTTON
        // ========================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    if (canContinue) {
                        ElyraTheme.colors.primary
                    } else {
                        ElyraTheme.colors.surfaceInteractive
                    }
                )
                .clickable(
                    enabled = canContinue
                ) {
                    onSendResetLink(email)
                },
            contentAlignment = Alignment.Center
        ) {


        authState.error?.let { error ->

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = error,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.error
            )
        }

        authState.message?.let { message ->

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = message,
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.textPrimary
            )
        }
            Text(
                text = "Send reset link",
                style = ElyraTheme.typography.labelLarge,
                color =
                    if (canContinue) {
                        ElyraTheme.colors.onPrimary
                    } else {
                        ElyraTheme.colors.textDisabled
                    }
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ========================================================
        // RETURN TO LOGIN
        // ========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Remember your password?",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Text(
                text = "  Sign in",
                modifier = Modifier
                    .clickable {
                        onSignIn()
                    }
                    .padding(
                        horizontal = 4.dp,
                        vertical = 8.dp
                    ),
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}