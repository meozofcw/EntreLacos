package com.entrelacos.arandu.ui.screens

import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.entrelacos.arandu.R
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkMain

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
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
                .fillMaxSize()
                .padding(horizontal = 28.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Entrar",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Entre na sua conta.",
                fontSize = 16.sp,
                color = DarkText.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                label = {
                    Text("Email")
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = {
                    senha = it
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                label = {
                    Text("Senha")
                },

                singleLine = true,

                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { },

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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Esqueceu sua senha?",
                color = PinkMain
            )
        }
    }
}