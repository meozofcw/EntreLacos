package com.entrelacos.arandu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.entrelacos.arandu.ui.AppNavigation
import com.entrelacos.arandu.ui.theme.EntreLacosTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            EntreLacosTheme {

                AppNavigation()

            }
        }
    }
}