package com.entrelacos.arandu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.entrelacos.arandu.ui.theme.PinkMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController
) {

    var postText by remember {
        mutableStateOf("")
    }

    var loading by remember {
        mutableStateOf(false)
    }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Nova Publicação")
                },

                navigationIcon = {

                    TextButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Voltar")
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                text = "Compartilhe sua experiência",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Sua história pode ajudar outras mães.",
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(

                value = postText,

                onValueChange = {

                    if (it.length <= 500) {
                        postText = it
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),

                placeholder = {
                    Text("No que você está pensando hoje?")
                },

                shape = RoundedCornerShape(20.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${postText.length}/500",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(

                onClick = {

                    if (postText.isBlank()) return@Button

                    loading = true

                    val user = auth.currentUser

                    val post = hashMapOf(

                        "userId" to (user?.uid ?: ""),

                        "userName" to (
                                user?.displayName ?: "Usuário"
                                ),

                        "userPhoto" to (
                                user?.photoUrl?.toString() ?: ""
                                ),

                        "text" to postText,

                        "likes" to 0,

                        "comments" to 0,

                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("posts")
                        .add(post)
                        .addOnSuccessListener {

                            loading = false

                            navController.popBackStack()
                        }
                        .addOnFailureListener {

                            loading = false
                        }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkMain
                ),

                shape = RoundedCornerShape(18.dp)
            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )

                } else {

                    Text(
                        text = "Publicar"
                    )
                }
            }
        }
    }
}