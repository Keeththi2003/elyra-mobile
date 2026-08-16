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

    // popBackStack() is a silent no-op on the start destination, so only show
    // the back arrow when there is somewhere to go.
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val canNavigateBack =
        currentBackStackEntry?.let {
            navController.previousBackStackEntry != null
        } ?: false

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SIGN_UP
    ) {

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

        composable(AuthRoutes.SIGN_IN) {

            LoginScreen(
                showBackButton = canNavigateBack,
                onBack = {
                    navController.popBackStack()
                },

                // Sign Up is the start destination, so it always sits below
                // Sign In. Popping keeps the two a toggle; navigating would
                // push a duplicate copy on every switch.
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
