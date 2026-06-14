package com.entrelacos.arandu.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entrelacos.arandu.model.SupportPoint
import com.entrelacos.arandu.repository.SupportPointRepository
import com.entrelacos.arandu.ui.components.SupportMap
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkMain

data class Course(val title: String, val hours: String)
data class Job(val title: String, val location: String)

@Composable
fun OpportunitiesTab() {

    val repository = remember { SupportPointRepository() }
    var supportPoints by remember { mutableStateOf<List<SupportPoint>>(emptyList()) }

    LaunchedEffect(Unit) {
        repository.getSupportPoints { supportPoints = it }
    }

    val courses = listOf(
        Course("Informática Básica", "20h"),
        Course("Educação Financeira", "15h"),
        Course("Introdução ao Autismo", "12h"),
        Course("Empreendedorismo Feminino", "25h")
    )

    val jobs = listOf(
        Job("Assistente Virtual", "Home Office"),
        Job("Atendimento WhatsApp", "Remoto"),
        Job("Recepcionista", "Teresina"),
        Job("Costureira", "Flexível")
    )

    // Column scrollável normal — MapView não funciona dentro de LazyColumn
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5F8))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Oportunidades",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = "Conectando mães a novas possibilidades",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Cursos
        Text(
            text = "📚 Cursos Gratuitos",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(courses) { course ->
                Card(
                    modifier = Modifier.width(220.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.School,
                            contentDescription = null,
                            tint = PinkMain
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = course.title, fontWeight = FontWeight.Bold, color = DarkText)
                        Text(text = course.hours, color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = PinkMain),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Ver Curso", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Empregos
        Text(
            text = "💼 Empregos Adaptados",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(12.dp))

        jobs.forEach { job ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PinkLight, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BusinessCenter,
                            contentDescription = null,
                            tint = PinkMain,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = job.title, fontWeight = FontWeight.Bold, color = DarkText)
                        Text(text = job.location, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Rede de Apoio
        Text(
            text = "📍 Rede de Apoio",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = DarkText
        )
        Text(
            text = "Pontos de suporte próximos a você",
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Mapa com altura fixa explícita
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            SupportMap(supportPoints = supportPoints)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de pontos
        if (supportPoints.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PinkLight)
            ) {
                Text(
                    text = "Nenhum ponto de apoio cadastrado ainda.",
                    modifier = Modifier.padding(16.dp),
                    color = PinkMain,
                    fontSize = 13.sp
                )
            }
        } else {
            supportPoints.forEach { point ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(PinkLight, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = PinkMain,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = point.name,
                                fontWeight = FontWeight.Bold,
                                color = DarkText,
                                fontSize = 13.sp
                            )
                            if (point.description.isNotEmpty()) {
                                Text(
                                    text = point.description,
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}