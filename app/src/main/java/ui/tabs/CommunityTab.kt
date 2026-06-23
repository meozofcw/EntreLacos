package com.entrelacos.arandu.ui.tabs

import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.entrelacos.arandu.model.Post
import com.entrelacos.arandu.repository.PostRepository
import com.entrelacos.arandu.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CommunityTab(
    navController: NavController
) {

    val user = FirebaseAuth.getInstance().currentUser

    val repository = remember { PostRepository() }
    val profileRepository = remember { UserProfileRepository() }

    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var myPhotoUrl by remember { mutableStateOf("") }
    var myName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        repository.getPosts { postsList -> posts = postsList }
        profileRepository.getProfile { profile ->
            if (profile != null) {
                myPhotoUrl = profile.photoUrl
                myName = profile.name
            }
        }
    }

    val composerPhoto = myPhotoUrl.takeIf { it.isNotEmpty() } ?: user?.photoUrl?.toString()
    val displayName = myName.takeIf { it.isNotEmpty() } ?: user?.displayName ?: "Mamãe"

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_post") }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Criar publicação"
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Olá, $displayName 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Como você está hoje?",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("create_post") },
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = composerPhoto,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(text = "No que você está pensando?")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Text(text = "Comunidades", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(
                        listOf("#Autismo", "#Maternidade", "#Inclusão", "#Educação", "#Saúde")
                    ) { tag ->
                        AssistChip(onClick = {}, label = { Text(tag) })
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            items(posts) { post ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = post.userPhoto,
                                contentDescription = null,
                                modifier = Modifier.size(42.dp).clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = post.userName, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = post.text)

                        if (post.imageUrl.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            AsyncImage(
                                model = post.imageUrl,
                                contentDescription = "Imagem da publicação",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.likes}")

                            Spacer(modifier = Modifier.width(20.dp))

                            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${post.comments}")

                            Spacer(modifier = Modifier.width(20.dp))

                            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}