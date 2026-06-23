package com.entrelacos.arandu.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.entrelacos.arandu.repository.StorageRepository
import com.entrelacos.arandu.repository.UserProfileRepository
import com.entrelacos.arandu.ui.components.PostImagePickerField
import com.entrelacos.arandu.ui.theme.PinkMain
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController
) {
    var postText by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var userPhotoUrl by remember { mutableStateOf("") }
    var userDisplayName by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storageRepository = remember { StorageRepository() }
    val profileRepository = remember { UserProfileRepository() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Busca a foto e nome cadastrados no perfil (Supabase), não a do Google
    LaunchedEffect(Unit) {
        profileRepository.getProfile { profile ->
            if (profile != null) {
                userPhotoUrl = profile.photoUrl
                if (profile.name.isNotEmpty()) userDisplayName = profile.name
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Publicação") },
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) {
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sua história pode ajudar outras mães.",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = postText,
                onValueChange = { if (it.length <= 500) postText = it },
                modifier = Modifier.fillMaxWidth().height(220.dp),
                placeholder = { Text("No que você está pensando hoje?") },
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${postText.length}/500",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PostImagePickerField(
                localPreviewUri = selectedImageUri,
                onImagePicked = { selectedImageUri = it },
                onRemove = { selectedImageUri = null }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (postText.isBlank()) return@Button

                    loading = true
                    val user = auth.currentUser
                    val uid = user?.uid ?: ""

                    scope.launch {
                        val imageUrl = selectedImageUri?.let { uri ->
                            storageRepository.uploadImage(
                                context = context,
                                uri = uri,
                                bucket = "posts",
                                uid = uid
                            )
                        } ?: ""

                        val finalName = userDisplayName.takeIf { it.isNotEmpty() }
                            ?: user?.displayName
                            ?: "Usuário"

                        val post = hashMapOf(
                            "userId" to uid,
                            "userName" to finalName,
                            "userPhoto" to userPhotoUrl,
                            "text" to postText,
                            "imageUrl" to imageUrl,
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
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkMain),
                shape = RoundedCornerShape(18.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Publicar")
                }
            }
        }
    }
}