package com.entrelacos.arandu.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import kotlinx.coroutines.launch

import com.entrelacos.arandu.R
import com.entrelacos.arandu.auth.GoogleAuthManager
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkMain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun CreateAccountScreen(
    navController: NavController
) {

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()

    val googleAuthManager = remember {
        GoogleAuthManager(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
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
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(horizontal = 28.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Criar Conta",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Faça parte da comunidade EntreMães",
                fontSize = 16.sp,
                color = DarkText.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                label = {
                    Text("Nome Completo")
                },

                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },

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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                label = {
                    Text("Senha")
                },

                singleLine = true,

                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                label = {
                    Text("Confirmar Senha")
                },

                singleLine = true,

                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    when {

                        nome.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Digite seu nome",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        email.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Digite seu email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        senha.length < 6 -> {
                            Toast.makeText(
                                context,
                                "A senha deve ter no mínimo 6 caracteres",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        senha != confirmarSenha -> {
                            Toast.makeText(
                                context,
                                "As senhas não coincidem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            auth.createUserWithEmailAndPassword(
                                email,
                                senha
                            ).addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    auth.currentUser?.updateProfile(
                                        UserProfileChangeRequest.Builder()
                                            .setDisplayName(nome)
                                            .build()
                                    )

                                    Toast.makeText(
                                        context,
                                        "Conta criada com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate("home") {
                                        popUpTo("create_account") {
                                            inclusive = true
                                        }
                                    }

                                } else {

                                    Toast.makeText(
                                        context,
                                        task.exception?.message
                                            ?: "Erro ao criar conta",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
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

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                HorizontalDivider(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "  ou  ",
                    color = Color.Gray
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {

                    scope.launch {

                        try {

                            val success =
                                googleAuthManager.signIn()

                            if (success) {

                                navController.navigate("home") {
                                    popUpTo("create_account") {
                                        inclusive = true
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()

                            Toast.makeText(
                                context,
                                "Erro ao entrar com Google",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(18.dp)
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.google_logo
                    ),
                    contentDescription = "Google",
                    modifier = Modifier.size(22.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = "Continuar com Google",
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {

                Text(
                    text = "Já possui conta? Entrar",
                    color = PinkMain
                )
            }
        }
    }
}