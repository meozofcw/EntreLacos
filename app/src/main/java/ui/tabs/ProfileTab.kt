package com.entrelacos.arandu.ui.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileTab(
    navController: NavController
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = {

                FirebaseAuth
                    .getInstance()
                    .signOut()

                navController.navigate("login") {
                    popUpTo("home") {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Sair")
        }
    }
}