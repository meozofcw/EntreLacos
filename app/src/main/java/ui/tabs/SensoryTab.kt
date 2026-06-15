package com.entrelacos.arandu.ui.tabs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.entrelacos.arandu.model.SensoryCategory
import com.entrelacos.arandu.model.SensoryContent
import com.entrelacos.arandu.repository.SensoryRepository
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkMain
import com.entrelacos.arandu.ui.theme.PinkSoft

@Composable
fun SensoryTab() {

    val repository = remember { SensoryRepository() }
    val context = LocalContext.current
    var allContent by remember { mutableStateOf<List<SensoryContent>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf(SensoryCategory.ALL) }
    var selectedContent by remember { mutableStateOf<SensoryContent?>(null) }

    LaunchedEffect(Unit) {
        repository.getContent { allContent = it }
    }

    val filtered = remember(allContent, selectedCategory) {
        if (selectedCategory == SensoryCategory.ALL) allContent
        else allContent.filter { it.category == selectedCategory.key }
    }

    val videos = filtered.filter { it.type == "video" }
    val activities = filtered.filter { it.type == "activity" }

    // Dialog de detalhe
    selectedContent?.let { content ->
        ContentDialog(
            content = content,
            onDismiss = { selectedContent = null },
            onRate = { stars ->
                repository.rateContent(content.id, stars)
                selectedContent = null
            },
            onOpenYoutube = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${content.youtubeId}"))
                context.startActivity(intent)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5F8))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PinkMain)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Column {
                Text(
                    text = "Espaço Sensorial ✨",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Atividades e vídeos para crianças com TEA",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // Filtros de categoria
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SensoryCategory.values().toList()) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = { Text("${category.emoji} ${category.label}", fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PinkMain,
                        selectedLabelColor = Color.White,
                        containerColor = PinkLight,
                        labelColor = DarkText
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = PinkSoft,
                        selectedBorderColor = PinkMain
                    )
                )
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            if (allContent.isEmpty()) {
                Spacer(modifier = Modifier.height(60.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Nenhum conteúdo cadastrado ainda.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {

                // Vídeos
                if (videos.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "🎬 Vídeos",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    videos.forEach { content ->
                        VideoCard(
                            content = content,
                            onClick = { selectedContent = content }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                // Atividades
                if (activities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "🧩 Atividades",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    activities.forEach { content ->
                        ActivityCard(
                            content = content,
                            onClick = { selectedContent = content }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                if (filtered.isEmpty() && allContent.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(40.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Nenhum conteúdo nessa categoria.", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun VideoCard(content: SensoryContent, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFFFEEF1), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = null,
                    tint = PinkMain,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content.title,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.description.isNotEmpty()) {
                    Text(
                        text = content.description,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (content.ageRange.isNotEmpty()) {
                        Surface(shape = RoundedCornerShape(20.dp), color = PinkLight) {
                            Text(
                                text = "${content.ageRange} anos",
                                fontSize = 10.sp,
                                color = PinkMain,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    StarRating(rating = content.rating, count = content.ratingCount)
                }
            }
        }
    }
}

@Composable
private fun ActivityCard(content: SensoryContent, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFEEEDFE), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Extension,
                    contentDescription = null,
                    tint = Color(0xFF534AB7),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = content.title,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.description.isNotEmpty()) {
                    Text(
                        text = content.description,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (content.ageRange.isNotEmpty()) {
                        Surface(shape = RoundedCornerShape(20.dp), color = Color(0xFFEEEDFE)) {
                            Text(
                                text = "${content.ageRange} anos",
                                fontSize = 10.sp,
                                color = Color(0xFF534AB7),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    StarRating(rating = content.rating, count = content.ratingCount)
                }
            }
        }
    }
}

@Composable
private fun StarRating(rating: Double, count: Int) {
    if (count == 0) return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFFFC107),
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "%.1f".format(rating),
            fontSize = 10.sp,
            color = Color.Gray
        )
        Text(
            text = " ($count)",
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun ContentDialog(
    content: SensoryContent,
    onDismiss: () -> Unit,
    onRate: (Int) -> Unit,
    onOpenYoutube: () -> Unit
) {
    var hoveredStar by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = content.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (content.description.isNotEmpty()) {
                    Text(
                        text = content.description,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (content.type == "activity" && content.activityContent.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PinkLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = content.activityContent,
                            fontSize = 13.sp,
                            color = DarkText,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (content.type == "video" && content.youtubeId.isNotEmpty()) {
                    Button(
                        onClick = onOpenYoutube,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Outlined.PlayCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Abrir no YouTube")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Avaliação
                Text(
                    text = "Avalie este conteúdo:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = if (star <= hoveredStar) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "$star estrelas",
                            tint = if (star <= hoveredStar) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    hoveredStar = star
                                    onRate(star)
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Fechar", color = PinkMain)
                }
            }
        }
    }
}