package com.entrelacos.arandu.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController

import kotlinx.coroutines.delay

import com.entrelacos.arandu.R
import com.entrelacos.arandu.ui.theme.PinkLight
import com.entrelacos.arandu.ui.theme.PinkSoft

@Composable
fun SplashScreen(navController: NavController) {

    var startAnimation by remember {
        mutableStateOf(false)
    }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(2000),
        label = ""
    )

    LaunchedEffect(true) {

        startAnimation = true

        delay(2500)

        navController.navigate("auth_check") {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PinkLight,
                        PinkSoft
                    )
                )
            ),

        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logonobg),
            contentDescription = "Logo",

            modifier = Modifier
                .size(220.dp)
                .alpha(alphaAnim.value),

            contentScale = ContentScale.Fit
        )
    }
}