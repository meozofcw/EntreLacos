package com.entrelacos.arandu.ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@SuppressLint("MissingPermission")
@Composable
fun SupportMap() {

    AndroidView(

        factory = { context ->

            MapView(context).apply {

                layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                getMapAsync { map ->

                    map.setStyle(
                        Style.Builder().fromUri(
                            "https://demotiles.maplibre.org/style.json"
                        )
                    )
                }
            }
        }
    )
}