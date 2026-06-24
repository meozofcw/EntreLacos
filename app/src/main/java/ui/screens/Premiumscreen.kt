package com.entrelacos.arandu.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.entrelacos.arandu.ui.theme.DarkText
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkMain

private enum class PlanOption(
    val label: String,
    val price: String,
    val period: String,
    val badge: String?
) {
    MONTHLY("Mensal", "R$ 49,90", "/mês", null),
    ANNUAL("Anual", "R$ 369,90", "/ano", "Economize 38%"),
    DAY_USE("Day Use", "R$ 15,00", "/dia", null)
}

@Composable
fun PremiumScreen(navController: NavController) {

    var selectedPlan by remember { mutableStateOf(PlanOption.ANNUAL) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF5F8))
            .verticalScroll(rememberScrollState())
    ) {

        // Header com gradiente escuro + dourado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF413E51), Color(0xFF6B5B73))
                    )
                )
                .padding(top = 48.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
        ) {
            Column {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "EntreLaços Premium",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Mais apoio, mais oportunidades, mais cuidado para você e seu filho",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            // Benefícios
            Text(
                text = "O que está incluso",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(14.dp))

            BenefitRow(
                icon = Icons.Outlined.LocalHospital,
                title = "Consultas médicas facilitadas",
                description = "Acesso prioritário a uma rede de profissionais parceiros"
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitRow(
                icon = Icons.Outlined.VideoCall,
                title = "Telemedicina",
                description = "Atendimento online rápido, sem sair de casa"
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitRow(
                icon = Icons.Outlined.School,
                title = "Cursos profissionalizantes",
                description = "Capacitação para gerar renda extra com horário flexível"
            )
            Spacer(modifier = Modifier.height(12.dp))

            BenefitRow(
                icon = Icons.Outlined.MenuBook,
                title = "Conteúdos exclusivos",
                description = "Materiais extras de apoio para mães e crianças atípicas"
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Planos
            Text(
                text = "Escolha seu plano",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(14.dp))

            PlanOption.values().forEach { plan ->
                PlanCard(
                    plan = plan,
                    isSelected = selectedPlan == plan,
                    onClick = { selectedPlan = plan }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* em breve: integração de pagamento */ },
                enabled = false,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),
                    disabledContainerColor = Color(0xFFFFD700).copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Em breve",
                    color = Color(0xFF413E51),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "O pagamento ainda não está disponível. Você será avisada quando o Premium for lançado.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun BenefitRow(icon: ImageVector, title: String, description: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(PinkLight, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PinkMain,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkText)
            Text(text = description, fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun PlanCard(
    plan: PlanOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PinkLight else Color.White
        ),
        border = if (isSelected)
            BorderStroke(2.dp, PinkMain)
        else
            BorderStroke(1.dp, Color(0xFFEFE0E5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = PinkMain)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DarkText
                    )
                    if (plan.badge != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = PinkMain
                        ) {
                            Text(
                                text = plan.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = plan.price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PinkMain
                )
                Text(
                    text = plan.period,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}