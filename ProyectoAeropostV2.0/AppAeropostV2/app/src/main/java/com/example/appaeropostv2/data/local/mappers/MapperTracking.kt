package com.example.appaeropostv2.data.local.mappers

import com.example.appaeropostv2.data.local.entity.EntityTracking
import com.example.appaeropostv2.domain.model.Tracking

fun EntityTracking.toDomain(): Tracking = Tracking(
    idTracking = idTracking,
    idPaquete = idPaquete,
    numeroTracking = numeroTracking,
    casilleroOrigen = casilleroOrigen,
    estado = estado,
    latitud = latitud,
    longitud = longitud,
    tiempoInicioMillis = tiempoInicioMillis,
    ultimaActualizacionMillis = ultimaActualizacionMillis
)

fun Tracking.toEntity(): EntityTracking = EntityTracking(
    idTracking = idTracking,
    idPaquete = idPaquete,
    numeroTracking = numeroTracking,
    casilleroOrigen = casilleroOrigen,
    estado = estado,
    latitud = latitud,
    longitud = longitud,
    tiempoInicioMillis = tiempoInicioMillis,
    ultimaActualizacionMillis = ultimaActualizacionMillis
)
