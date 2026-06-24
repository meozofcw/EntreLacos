package com.entrelacos.arandu.ui.tabs

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.entrelacos.arandu.model.UserProfile
import com.entrelacos.arandu.repository.StorageRepository
import com.entrelacos.arandu.repository.UserProfileRepository
import com.entrelacos.arandu.ui.components.AvatarPickerField
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkMain
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ProfileTab(navController: NavController) {

    val repository = remember { UserProfileRepository() }
    val storageRepository = remember { StorageRepository() }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var localAvatarUri by remember { mutableStateOf<Uri?>(null) }
    var uploadingAvatar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        repository.getProfile { profile = it }
    }

    val displayName = profile?.name?.takeIf { it.isNotEmpty() }
        ?: currentUser?.email?.substringBefore("@")
        ?: "Mãe EntreLaços"

    if (showEditDialog) {
        EditProfileDialog(
            profile = profile ?: UserProfile(),
            onDismiss = { showEditDialog = false },
            onSave = { updated ->
                repository.saveProfile(updated) { success ->
                    if (success) {
                        profile = updated
                        showEditDialog = false
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5F8))
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PinkMain)
                .padding(top = 32.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box {
                    // key força recomposição do AsyncImage quando a URL muda
                    key(profile?.photoUrl) {
                        AvatarPickerField(
                            currentImageUrl = profile?.photoUrl?.takeIf { it.isNotEmpty() },
                            localPreviewUri = localAvatarUri,
                            size = 80,
                            onImagePicked = { uri ->
                                localAvatarUri = uri
                                uploadingAvatar = true

                                val uid = currentUser?.uid ?: ""
                                scope.launch {
                                    val url = storageRepository.uploadImage(
                                        context = context,
                                        uri = uri,
                                        bucket = "avatars",
                                        uid = uid
                                    )
                                    uploadingAvatar = false
                                    if (url != null) {
                                        repository.updatePhotoUrl(url)
                                        profile = (profile ?: UserProfile()).copy(photoUrl = url)
                                        localAvatarUri = null
                                    }
                                }
                            }
                        )
                    }

                    if (uploadingAvatar) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Black.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = displayName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (profile?.city?.isNotEmpty() == true) {
                    Text(
                        text = profile!!.city,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            if (profile?.bio?.isNotEmpty() == true) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = profile!!.bio,
                        modifier = Modifier.padding(14.dp),
                        fontSize = 13.sp,
                        color = DarkText,
                        lineHeight = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            PremiumCard(onClick = { navController.navigate("premium") })

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "👶 Meu filho(a)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (profile?.childName?.isNotEmpty() == true) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(PinkLight, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChildCare,
                                contentDescription = null,
                                tint = PinkMain
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = profile!!.childName,
                                fontWeight = FontWeight.Bold,
                                color = DarkText,
                                fontSize = 14.sp
                            )
                            val details = listOfNotNull(
                                profile!!.childAge.takeIf { it.isNotEmpty() }?.let { "$it anos" },
                                profile!!.childCondition.takeIf { it.isNotEmpty() }
                            ).joinToString(" · ")
                            if (details.isNotEmpty()) {
                                Text(text = details, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PinkLight)
                ) {
                    Text(
                        text = "Adicione os dados do seu filho(a) para personalizar sua experiência.",
                        modifier = Modifier.padding(14.dp),
                        fontSize = 12.sp,
                        color = PinkMain
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "⚙️ Configurações",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Outlined.Edit,
                        label = "Editar perfil",
                        onClick = { showEditDialog = true }
                    )

                    HorizontalDivider(color = Color(0xFFF0E0E5))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Notificações",
                            fontSize = 13.sp,
                            color = DarkText,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = profile?.notificationsEnabled ?: true,
                            onCheckedChange = { enabled ->
                                repository.updateNotifications(enabled)
                                profile = profile?.copy(notificationsEnabled = enabled)
                            },
                            colors = SwitchDefaults.colors(checkedTrackColor = PinkMain)
                        )
                    }

                    HorizontalDivider(color = Color(0xFFF0E0E5))

                    SettingsRow(
                        icon = Icons.Outlined.Logout,
                        label = "Sair",
                        labelColor = Color(0xFFE24B4A),
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun PremiumCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF413E51), Color(0xFF6B5B73))
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EntreLaços Premium",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Consultas médicas, telemedicina e cursos profissionalizantes para você.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    labelColor: Color = DarkText,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onClick, contentPadding = PaddingValues(0.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (labelColor == DarkText) Color.Gray else labelColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = label, fontSize = 13.sp, color = labelColor)
            }
        }
    }
}

@Composable
private fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var bio by remember { mutableStateOf(profile.bio) }
    var city by remember { mutableStateOf(profile.city) }
    var childName by remember { mutableStateOf(profile.childName) }
    var childAge by remember { mutableStateOf(profile.childAge) }
    var childCondition by remember { mutableStateOf(profile.childCondition) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Editar perfil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Seu nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pinkTextFieldColors()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Cidade") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pinkTextFieldColors()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Sobre você") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    colors = pinkTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Dados do seu filho(a)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = childName,
                    onValueChange = { childName = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pinkTextFieldColors()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = childAge,
                    onValueChange = { childAge = it },
                    label = { Text("Idade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = pinkTextFieldColors()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = childCondition,
                    onValueChange = { childCondition = it },
                    label = { Text("Condição (ex: TEA nível 1)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pinkTextFieldColors()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                profile.copy(
                                    name = name,
                                    bio = bio,
                                    city = city,
                                    childName = childName,
                                    childAge = childAge,
                                    childCondition = childCondition
                                )
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkMain),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}

@Composable
private fun pinkTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PinkMain,
    focusedLabelColor = PinkMain,
    cursorColor = PinkMain
)