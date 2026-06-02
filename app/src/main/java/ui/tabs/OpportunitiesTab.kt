package com.entrelacos.arandu.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.entrelacos.arandu.ui.components.SupportMap

data class Course(
    val title: String,
    val hours: String
)

data class Job(
    val title: String,
    val location: String
)

data class SupportPoint(
    val name: String,
    val distance: String
)

@Composable
fun OpportunitiesTab() {

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

    val supportPoints = listOf(
        SupportPoint("UBS Promorar", "1.2 km"),
        SupportPoint("Delegacia da Mulher", "2.8 km"),
        SupportPoint("APAE Teresina", "3.4 km"),
        SupportPoint("CRAS Sul", "1.9 km")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {

        item {

            Text(
                text = "Oportunidades",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Conectando mães a novas possibilidades",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {

            Text(
                text = "📚 Cursos Gratuitos",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(courses) { course ->

                    Card(
                        modifier = Modifier.width(220.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.School,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = course.title,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = course.hours,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = { }
                            ) {
                                Text("Ver Curso")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {

            Text(
                text = "💼 Empregos Adaptados",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        items(jobs) { job ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),

                shape = RoundedCornerShape(20.dp)
            ) {

                Row(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.BusinessCenter,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = job.title,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = job.location,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "📍 Rede de Apoio",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),

                shape = RoundedCornerShape(20.dp)
            ) {

                SupportMap()
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        items(supportPoints) { point ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),

                shape = RoundedCornerShape(20.dp)
            ) {

                Row(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {

                        Text(
                            text = point.name,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = point.distance,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "⭐ Recomendado para você",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("📚 Introdução ao Autismo")
                    Text("💼 Atendimento Home Office")
                    Text("📍 UBS mais próxima")

                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}