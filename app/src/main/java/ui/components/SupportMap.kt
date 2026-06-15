package com.entrelacos.arandu.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
    var userLatLng by remember { mutableStateOf<LatLng?>(null) }

    val hasPermission = remember {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

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
                    map.setStyle("https://tiles.openfreemap.org/styles/liberty") {

                        if (hasPermission) {
                            val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                            val location = listOf(
                                LocationManager.GPS_PROVIDER,
                                LocationManager.NETWORK_PROVIDER,
                                LocationManager.PASSIVE_PROVIDER
                            )
                                .filter { locationManager.isProviderEnabled(it) }
                                .mapNotNull { locationManager.getLastKnownLocation(it) }
                                .maxByOrNull { it.time }

                            location?.let {
                                val latLng = LatLng(it.latitude, it.longitude)
                                userLatLng = latLng

                                // Zoom 13 = nível de cidade/bairro
                                map.animateCamera(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.Builder()
                                            .target(latLng)
                                            .zoom(13.0)
                                            .build()
                                    )
                                )

                                // Marker azul para o usuário
                                map.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title("Você está aqui")
                                        .icon(
                                            IconFactory.getInstance(ctx)
                                                .fromBitmap(createUserMarker())
                                        )
                                )
                            }
                        }
                    }
                }
                mapView = this
            }
        }
    )

    // Plota os pontos de apoio quando chegarem do Firestore
    LaunchedEffect(supportPoints, mapRef) {
        val map = mapRef ?: return@LaunchedEffect
        if (supportPoints.isEmpty()) return@LaunchedEffect

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

// Marker azul para indicar posição do usuário
private fun createUserMarker(): Bitmap {
    val size = 40
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val cx = size / 2f
    val cy = size / 2f

    // Círculo externo branco
    canvas.drawCircle(cx, cy, size * 0.48f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    })
    // Círculo azul
    canvas.drawCircle(cx, cy, size * 0.34f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.parseColor("#4A90E2")
        style = Paint.Style.FILL
    })
    // Ponto central branco
    canvas.drawCircle(cx, cy, size * 0.12f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    })

    return bitmap
}