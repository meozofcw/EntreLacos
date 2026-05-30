package com.entrelacos.arandu.ui.screens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthCheckScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {

        val user = FirebaseAuth
            .getInstance()
            .currentUser

        if (user != null) {

            navController.navigate("home") {
                popUpTo("auth_check") {
                    inclusive = true
                }
            }

        } else {

            navController.navigate("welcome") {
                popUpTo("auth_check") {
                    inclusive = true
                }
            }
        }
    }
}