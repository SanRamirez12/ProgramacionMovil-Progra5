package com.example.appaeropostv2.domain.model

import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.TrackingStatus

data class Tracking(
    val idTracking: Long = 0,
    val idPaquete: String,
    val numeroTracking: String,
    val casilleroOrigen: Casilleros,
    val estado: TrackingStatus,
    val latitud: Double,
    val longitud: Double,
    val tiempoInicioMillis: Long,
    val ultimaActualizacionMillis: Long
)
