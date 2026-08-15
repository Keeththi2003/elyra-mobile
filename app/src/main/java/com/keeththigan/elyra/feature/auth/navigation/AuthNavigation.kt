package com.keeththigan.elyra.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keeththigan.elyra.feature.auth.login.LoginScreen
import com.keeththigan.elyra.feature.auth.signup.SignUpScreen
import com.keeththigan.elyra.feature.auth.AuthViewModel

@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel 
) {

        val navController = androidx.navigation.compose.rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "signup"
    ) {

        // ============================================================
        // SIGN UP
        // ============================================================

        composable("signup") {

            SignUpScreen(
                onBack = {
                    navController.popBackStack()
                },

                onSignIn = {
                    navController.navigate("signin")
                },

                onCreateAccount = {
                    // Authentication succeeded.
                    // Return this to your main/home navigation later.
                    navController.navigate("home") {
                        popUpTo("signup") {
                            inclusive = true
                        }
                    }
                },

                authViewModel = authViewModel
            )
        }

        // ============================================================
        // SIGN IN
        // ============================================================

        composable("signin") {

    LoginScreen(
        onBack = {
            navController.popBackStack()
        },

        onSignUp = {
            navController.navigate("signup")
        },

        onLogin = {
            // We will connect Firebase login here
        },

        onForgotPassword = {
            navController.navigate("forgot_password")
        },

        authViewModel = authViewModel
    )
}
    }
}