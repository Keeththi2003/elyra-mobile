package com.keeththigan.elyra.feature.auth.signup

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

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    val passwordsMatch =
        password == confirmPassword

    val canCreateAccount =
        name.isNotBlank() &&
        email.isNotBlank() &&
        password.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        passwordsMatch

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
                    tint = ElyraTheme.colors.textPrimary,
                    modifier = Modifier.size(21.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        // ========================================================
        // HEADER
        // ========================================================

        Text(
            text = "Create your account",
            style = ElyraTheme.typography.displaySmall,
            color = ElyraTheme.colors.textPrimary
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Set up Elyra and start managing your home.",
            style = ElyraTheme.typography.bodyLarge,
            color = ElyraTheme.colors.textSecondary
        )

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        // ========================================================
        // FORM
        // ========================================================

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            ElyraSignUpField(
                label = "Full name",
                value = name,
                onValueChange = {
                    name = it
                },
                placeholder = "Your name",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )

            ElyraSignUpField(
                label = "Email",
                value = email,
                onValueChange = {
                    email = it
                },
                placeholder = "you@example.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            ElyraSignUpField(
                label = "Password",
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = "Create a password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = {
                    passwordVisible = !passwordVisible
                }
            )

            ElyraSignUpField(
                label = "Confirm password",
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                placeholder = "Repeat your password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                passwordVisible = confirmPasswordVisible,
                onPasswordVisibilityChange = {
                    confirmPasswordVisible = !confirmPasswordVisible
                }
            )
        }

        // ========================================================
        // PASSWORD VALIDATION
        // ========================================================

        if (
            confirmPassword.isNotEmpty() &&
            !passwordsMatch
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Passwords don't match.",
                style = ElyraTheme.typography.bodySmall,
                color = ElyraTheme.colors.error
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // ========================================================
        // CREATE ACCOUNT
        // ========================================================

        ElyraCreateAccountButton(
            enabled = canCreateAccount,
            onClick = onCreateAccount
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // ========================================================
        // TERMS
        // ========================================================

        Text(
            text = "By creating an account, you agree to our Terms of Service and Privacy Policy.",
            modifier = Modifier.fillMaxWidth(),
            style = ElyraTheme.typography.bodySmall,
            color = ElyraTheme.colors.textTertiary
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // ========================================================
        // SIGN IN
        // ========================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Already have an account?",
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


// =================================================================
// SIGN UP FIELD
// =================================================================

@Composable
private fun ElyraSignUpField(
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
// CREATE ACCOUNT BUTTON
// =================================================================

@Composable
private fun ElyraCreateAccountButton(
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
            text = "Create account",
            style = ElyraTheme.typography.labelLarge,
            color = content
        )
    }
}