package com.keeththigan.elyra.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.keeththigan.elyra.feature.auth.login.LoginScreen
import com.keeththigan.elyra.feature.auth.signup.SignUpScreen
import com.keeththigan.elyra.feature.auth.AuthViewModel
import com.keeththigan.elyra.feature.auth.forgotpassword.ForgotPasswordScreen

private object AuthRoutes {
    const val SIGN_UP = "signup"
    const val SIGN_IN = "signin"
    const val FORGOT_PASSWORD = "forgot_password"
}

@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel
) {

    val navController = androidx.navigation.compose.rememberNavController()

    // Re-evaluated on every navigation change so the back button is only
    // shown when there is actually somewhere to go back to. On the start
    // destination, navController.popBackStack() is a silent no-op, which
    // otherwise makes the back arrow look broken.
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val canNavigateBack =
        currentBackStackEntry?.let {
            navController.previousBackStackEntry != null
        } ?: false

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SIGN_UP
    ) {

        // ============================================================
        // SIGN UP
        // ============================================================

        composable(AuthRoutes.SIGN_UP) {

            SignUpScreen(
                showBackButton = canNavigateBack,
                onBack = {
                    navController.popBackStack()
                },

                onSignIn = {
                    navController.navigate(AuthRoutes.SIGN_IN) {
                        launchSingleTop = true
                    }
                },

                onCreateAccount = {
                    // The root ElyraApp switches to the main app when authState changes.
                },

                authViewModel = authViewModel
            )
        }

        // ============================================================
        // SIGN IN
        // ============================================================

        composable(AuthRoutes.SIGN_IN) {

            LoginScreen(
                showBackButton = canNavigateBack,
                onBack = {
                    navController.popBackStack()
                },

                /*
                 * Sign Up is the start destination, so it is always already
                 * below Sign In on the stack. Popping back to it keeps the
                 * two screens a simple toggle — navigating instead would push
                 * a new copy every time and make "back" need several presses.
                 */
                onSignUp = {
                    navController.popBackStack(
                        AuthRoutes.SIGN_UP,
                        /* inclusive = */ false
                    )
                },

                onLogin = {
                    // The root ElyraApp switches to the main app when authState changes.
                },

                onForgotPassword = {
                    navController.navigate(AuthRoutes.FORGOT_PASSWORD) {
                        launchSingleTop = true
                    }
                },

                authViewModel = authViewModel
            )
        }

        // ============================================================
        // FORGOT PASSWORD
        // ============================================================

        composable(AuthRoutes.FORGOT_PASSWORD) {

            ForgotPasswordScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSendResetLink = { email ->
                    authViewModel.sendPasswordResetEmail(email)
                },
                onSignIn = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }
    }
}