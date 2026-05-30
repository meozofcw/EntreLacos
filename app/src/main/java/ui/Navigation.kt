package com.entrelacos.arandu.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.entrelacos.arandu.ui.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }
        composable("create_account") {
            CreateAccountScreen(navController)
        }
    }
    }