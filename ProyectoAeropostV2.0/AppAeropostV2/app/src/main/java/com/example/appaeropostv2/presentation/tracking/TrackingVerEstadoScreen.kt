package com.example.appaeropostv2.presentation.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appaeropostv2.domain.enums.TrackingStatus
import com.example.appaeropostv2.domain.logic.CoordinatesLogic
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.roundToInt

@Composable
fun TrackingVerEstadoScreen(
    numeroTracking: String,
    viewModel: TrackingViewModel,
    onBack: () -> Unit
) {
    val detalle = viewModel.detalleState

    LaunchedEffect(numeroTracking) {
        viewModel.cargarDetalle(numeroTracking)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(onClick = onBack) { Text("Volver") }

        if (detalle.cargando) {
            CircularProgressIndicator()
            return@Column
        }

        detalle.error?.let {
            Text(it)
            return@Column
        }

        val tracking = detalle.tracking ?: return@Column

        Text("Paquete: ${tracking.numeroTracking}")

        TrackingStepsBar(estadoActual = tracking.estado)

        DistanciaAlPaquete(
            trackingLat = tracking.latitud,
            trackingLng = tracking.longitud
        )

        val posPaquete = LatLng(tracking.latitud, tracking.longitud)
        val cameraState = rememberCameraPositionState()

        LaunchedEffect(posPaquete) {
            cameraState.animate(
                update = CameraUpdateFactory.newLatLngZoom(posPaquete, 10f),
                durationMs = 650
            )
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            cameraPositionState = cameraState
        ) {
            Marker(
                state = MarkerState(position = posPaquete),
                title = "Paquete",
                snippet = tracking.estado.etiqueta
            )
        }

        if (tracking.estado == TrackingStatus.DELIVERED) {
            Text("✅ Paquete recibido, pasar a recoger.")
        }
    }
}

@Composable
private fun TrackingStepsBar(estadoActual: TrackingStatus) {
    val pasos = listOf(
        TrackingStatus.ORDERED,
        TrackingStatus.PACKED,
        TrackingStatus.IN_TRANSIT,
        TrackingStatus.DELIVERED
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Estado: ${estadoActual.etiqueta}")
        pasos.forEach { estado ->
            val marcado = estado.paso <= estadoActual.paso
            Text(if (marcado) "🟦 ${estado.etiqueta}" else "⬜ ${estado.etiqueta}")
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun DistanciaAlPaquete(
    trackingLat: Double,
    trackingLng: Double
) {
    var permiso by remember { mutableStateOf(false) }
    var ubicacionActual by remember { mutableStateOf<Location?>(null) }
    var distanciaTexto by remember { mutableStateOf("Distancia: calculando...") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permiso = (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (result[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val contexto = LocalContext.current
    val fused = remember { LocationServices.getFusedLocationProviderClient(contexto) }

    // 📡 Pedimos actualizaciones reales (si existen)
    DisposableEffect(permiso) {
        if (!permiso) return@DisposableEffect onDispose {}

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3_000L
        ).setMinUpdateIntervalMillis(2_000L).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                ubicacionActual = result.lastLocation
            }
        }

        fused.requestLocationUpdates(request, callback, contexto.mainLooper)

        onDispose {
            fused.removeLocationUpdates(callback)
        }
    }

    // Cálculo de distancia (con fallback)
    LaunchedEffect(ubicacionActual, trackingLat, trackingLng) {

        // 1) Ubicación real si existe
        val origen = ubicacionActual ?: run {
            // 2) Fallback: Universidad Latina (modo demo)
            val fake = Location("fallback").apply {
                latitude = CoordinatesLogic.UNIVERSIDAD_LATINA.latitud
                longitude = CoordinatesLogic.UNIVERSIDAD_LATINA.longitud
            }
            distanciaTexto = "Distancia: calculando..."
            fake
        }

        val resultados = FloatArray(1)
        Location.distanceBetween(
            origen.latitude,
            origen.longitude,
            trackingLat,
            trackingLng,
            resultados
        )

        val metros = resultados[0]

        distanciaTexto = when {
            metros >= 1000f ->
                "Distancia: ${(metros / 1000f).toInt()} km aprox."
            else ->
                "Distancia: ${metros.toInt()} m aprox."
        }

        if (ubicacionActual == null) {
            distanciaTexto += " (ubicación simulada)"
        }
    }

    Text(distanciaTexto)
}
