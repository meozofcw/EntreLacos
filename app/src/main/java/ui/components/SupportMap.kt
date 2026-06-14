package com.entrelacos.arandu.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.entrelacos.arandu.model.SupportPoint
import org.maplibre.android.annotations.IconFactory
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

@SuppressLint("MissingPermission")
@Composable
fun SupportMap(supportPoints: List<SupportPoint>) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                onCreate(null)
                getMapAsync { map ->
                    mapRef = map
                    map.setStyle("https://demotiles.maplibre.org/style.json")
                }
                mapView = this
            }
        }
    )

    // Reagir quando os pontos chegarem do Firestore
    LaunchedEffect(supportPoints, mapRef) {
        val map = mapRef ?: return@LaunchedEffect
        if (supportPoints.isEmpty()) return@LaunchedEffect

        map.clear()

        val firstPoint = supportPoints.first()
        map.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(LatLng(firstPoint.latitude, firstPoint.longitude))
                    .zoom(11.8)
                    .build()
            )
        )

        supportPoints.forEach { point ->
            if (point.latitude != 0.0 && point.longitude != 0.0) {
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(point.latitude, point.longitude))
                        .title(point.name)
                        .snippet(point.description)
                )
            }
        }
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START   -> mapView?.onStart()
                Lifecycle.Event.ON_RESUME  -> mapView?.onResume()
                Lifecycle.Event.ON_PAUSE   -> mapView?.onPause()
                Lifecycle.Event.ON_STOP    -> mapView?.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView?.onDestroy()
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}