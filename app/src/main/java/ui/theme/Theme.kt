package com.entrelacos.arandu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EntreLacosColors = lightColorScheme(

    primary = PinkMain,
    secondary = PinkSoft,

    background = PinkLight,
    surface = PinkLight,

    onPrimary = DarkText,
    onSecondary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText
)

@Composable
fun EntreLacosTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = EntreLacosColors,
        content = content
    )
}