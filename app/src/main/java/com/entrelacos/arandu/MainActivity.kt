package com.entrelacos.arandu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

import com.entrelacos.arandu.ui.AppNavigation
import com.entrelacos.arandu.ui.theme.EntreLacosTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapLibre.getInstance(this, "", WellKnownTileServer.MapLibre)

        setContent {

            EntreLacosTheme {

                AppNavigation()

            }
        }
    }
}