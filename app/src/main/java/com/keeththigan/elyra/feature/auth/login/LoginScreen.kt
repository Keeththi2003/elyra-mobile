package com.keeththigan.elyra.feature.auth.login

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.keeththigan.elyra.core.designsystem.ElyraTheme
import com.keeththigan.elyra.feature.auth.AuthViewModel


@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    onLogin: () -> Unit,
    authViewModel: AuthViewModel
)  {

    // ========================================================
    // STATE
    // ========================================================

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // ========================================================
    // AUTH STATE
    // ========================================================

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            onLogin()
        }
    }

    // ========================================================
    // ========================================================
    // CAN LOGIN
    // ========================================================

    val canLogin =
        email.isNotBlank() &&
                password.isNotBlank()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ElyraTheme.colors.background)
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
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
                    tint = ElyraTheme.colors.textPrimary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // ========================================================
        // BRAND
        // ========================================================

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElyraTheme.colors.primary),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "E",
                    style = ElyraTheme.typography.titleLarge,
                    color = ElyraTheme.colors.onPrimary
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = "Welcome back.",
                style = ElyraTheme.typography.displaySmall,
                color = ElyraTheme.colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Sign in to continue to Elyra.",
                style = ElyraTheme.typography.bodyLarge,
                color = ElyraTheme.colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // ========================================================
        // FORM
        // ========================================================

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ====================================================
            // EMAIL
            // ====================================================

            ElyraAuthTextField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it

                    // Clear previous error when user edits
                    if (authState.error != null) {
                        authViewModel.clearError()
                    }
                },
                placeholder = "you@example.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            // ====================================================
            // PASSWORD
            // ====================================================

            ElyraAuthTextField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it

                    // Clear previous error when user edits
                    if (authState.error != null) {
                        authViewModel.clearError()
                    }
                },
                placeholder = "Enter your password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = {
                    passwordVisible = !passwordVisible
                }
            )
        }

        // ========================================================
        // ERROR
        // ========================================================

        if (authState.error != null) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = authState.error ?: "",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textPrimary
            )
        }

        // ========================================================
        // FORGOT PASSWORD
        // ========================================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {

            Text(
                text = "Forgot password?",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(
                        onClick = onForgotPassword
                    )
                    .padding(vertical = 8.dp),
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ========================================================
        // SIGN IN BUTTON
        // ========================================================

        ElyraPrimaryButton(
            text = if (authState.isLoading) {
                "Signing in..."
            } else {
                "Sign in"
            },
            enabled = canLogin && !authState.isLoading,
            onClick = {

                authViewModel.signIn(
                    email = email.trim(),
                    password = password
                )
            }
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ========================================================
        // SIGN UP
        // ========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "New to Elyra?",
                style = ElyraTheme.typography.bodyMedium,
                color = ElyraTheme.colors.textSecondary
            )

            Text(
                text = "  Create an account",
                modifier = Modifier
                    .clickable(
                        onClick = onSignUp
                    )
                    .padding(vertical = 8.dp),
                style = ElyraTheme.typography.labelLarge,
                color = ElyraTheme.colors.textPrimary
            )
        }
    }
}


// =================================================================
// AUTH TEXT FIELD
// =================================================================

@Composable
private fun ElyraAuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}
) {

    var focused by remember {
        mutableStateOf(false)
    }

    val borderColor =
        if (focused) {
            ElyraTheme.colors.textPrimary
        } else {
            ElyraTheme.colors.border
        }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            style = ElyraTheme.typography.labelLarge,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .onFocusChanged {
                    focused = it.isFocused
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation =
                if (isPassword && !passwordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
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
                            color = borderColor,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {

                        if (value.isEmpty()) {

                            Text(
                                text = placeholder,
                                style = ElyraTheme.typography.bodyLarge,
                                color = ElyraTheme.colors.textTertiary
                            )
                        }

                        innerTextField()
                    }

                    if (isPassword) {

                        IconButton(
                            onClick = onPasswordVisibilityChange,
                            modifier = Modifier.size(40.dp)
                        ) {

                            Icon(
                                imageVector =
                                    if (passwordVisible) {
                                        Icons.Outlined.VisibilityOff
                                    } else {
                                        Icons.Outlined.Visibility
                                    },
                                contentDescription =
                                    if (passwordVisible) {
                                        "Hide password"
                                    } else {
                                        "Show password"
                                    },
                                tint = ElyraTheme.colors.textSecondary
                            )
                        }
                    }
                }
            }
        )
    }
}


// =================================================================
// PRIMARY BUTTON
// =================================================================

@Composable
private fun ElyraPrimaryButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {

    val background =
        if (enabled) {
            ElyraTheme.colors.primary
        } else {
            ElyraTheme.colors.surfaceInteractive
        }

    val content =
        if (enabled) {
            ElyraTheme.colors.onPrimary
        } else {
            ElyraTheme.colors.textDisabled
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .background(background)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            style = ElyraTheme.typography.labelLarge,
            color = content
        )
    }
}