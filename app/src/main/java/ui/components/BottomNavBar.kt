package com.entrelacos.arandu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.entrelacos.arandu.R
import com.entrelacos.arandu.ui.theme.PinkMain

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 28.dp
            ),

        shape = RoundedCornerShape(38.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 18.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 8.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomItem(
                icon = R.drawable.ic_home,
                label = "Comunidade",
                selected = selectedIndex == 0,
                modifier = Modifier.weight(1f)
            ) {
                onItemSelected(0)
            }

            BottomItem(
                icon = R.drawable.ic_opportunities,
                label = "Oportun.",
                selected = selectedIndex == 1,
                modifier = Modifier.weight(1f)
            ) {
                onItemSelected(1)
            }

            BottomItem(
                icon = R.drawable.ic_sensorial,
                label = "Sensorial",
                selected = selectedIndex == 2,
                modifier = Modifier.weight(1f)
            ) {
                onItemSelected(2)
            }

            BottomItem(
                icon = R.drawable.ic_profile,
                label = "Perfil",
                selected = selectedIndex == 3,
                modifier = Modifier.weight(1f)
            ) {
                onItemSelected(3)
            }
        }
    }
}

@Composable
private fun BottomItem(
    icon: Int,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val color =
        if (selected)
            PinkMain
        else
            Color.Gray

    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 4.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.size(34.dp),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight =
                if (selected)
                    FontWeight.Bold
                else
                    FontWeight.Normal,

            maxLines = 1
        )
    }
}