package com.entrelacos.arandu.ui.screens

import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.entrelacos.arandu.R
import com.entrelacos.arandu.ui.theme.PinkMain
import com.entrelacos.arandu.ui.theme.DarkText

@Composable
fun WelcomeScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Image(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 28.dp)
                .padding(top = 0.dp),

            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Bem-Vinda",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Você não está sozinha.",
                fontSize = 16.sp,
                color = DarkText.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { navController.navigate("login") },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkMain
                )
            ) {

                Text(
                    text = "Entrar",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "OU",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = DarkText.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    navController.navigate("create_account")
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkMain
                )
            ) {

                Text(
                    text = "Criar Conta",
                    fontSize = 16.sp
                )
            }
        }
    }
}