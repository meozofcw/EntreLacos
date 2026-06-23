package com.entrelacos.arandu.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkMain

/**
 * Avatar circular clicável que abre a galeria e mostra preview da imagem escolhida.
 * Passa a Uri local escolhida via onImagePicked — o upload é feito por quem usa o componente.
 */
@Composable
fun AvatarPickerField(
    currentImageUrl: String?,
    localPreviewUri: Uri?,
    size: Int = 80,
    onImagePicked: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImagePicked(it) }
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(Color.White, CircleShape)
            .clickable {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val imageToShow = localPreviewUri ?: currentImageUrl

        if (imageToShow != null) {
            AsyncImage(
                model = imageToShow,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(size.dp).clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto,
                contentDescription = "Adicionar foto",
                tint = PinkMain,
                modifier = Modifier.size((size * 0.4).dp)
            )
        }

        // Badge de câmera no canto
        Box(
            modifier = Modifier
                .size((size * 0.32).dp)
                .align(Alignment.BottomEnd)
                .background(PinkMain, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size((size * 0.18).dp)
            )
        }
    }
}

/**
 * Botão quadrado para selecionar foto em um post (não circular).
 */
@Composable
fun PostImagePickerField(
    localPreviewUri: Uri?,
    onImagePicked: (Uri) -> Unit,
    onRemove: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImagePicked(it) }
    }

    if (localPreviewUri != null) {
        Box(
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = localPreviewUri,
                contentDescription = "Foto do post",
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp))
            )
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd)
                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    .clickable { onRemove() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Remover",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PinkLight)
                .clickable {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto,
                contentDescription = "Adicionar foto",
                tint = PinkMain,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}