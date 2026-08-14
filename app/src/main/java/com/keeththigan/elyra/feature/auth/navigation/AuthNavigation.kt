package com.keeththigan.elyra.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keeththigan.elyra.feature.auth.login.LoginScreen
import com.keeththigan.elyra.feature.auth.onboarding.OnboardingScreen

private object AuthRoutes {

    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
}

@Composable
fun AuthNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthRoutes.ONBOARDING
    ) {

        // ========================================================
        // ONBOARDING
        // ========================================================

        composable(AuthRoutes.ONBOARDING) {

            OnboardingScreen(

                onGetStarted = {
                    navController.navigate(
                        AuthRoutes.SIGN_UP
                    )
                },

                onSignIn = {
                    navController.navigate(
                        AuthRoutes.LOGIN
                    )
                }
            )
        }

        // ========================================================
        // LOGIN
        // ========================================================

        composable(AuthRoutes.LOGIN) {

            LoginScreen(

                onBack = {
                    navController.popBackStack()
                },

                onSignUp = {
                    navController.navigate(
                        AuthRoutes.SIGN_UP
                    )
                },

                onForgotPassword = {
                    // Later
                },

                onLogin = {
                    // Later
                }
            )
        }

        // ========================================================
        // SIGN UP
        // ========================================================

        composable(AuthRoutes.SIGN_UP) {

            // SignUpScreen will be added next.
        }
    }
}