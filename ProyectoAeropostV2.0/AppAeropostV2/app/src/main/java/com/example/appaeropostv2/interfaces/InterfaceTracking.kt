package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.model.Tracking

interface InterfaceTracking {

    suspend fun crearTracking(
        idPaquete: String,
        numeroTracking: String,
        casilleroOrigen: Casilleros,
        tiempoInicioMillis: Long //
    )

    suspend fun obtenerTrackingsPorCedula(cedula: String): List<Tracking>

    suspend fun obtenerTrackingPorNumero(numeroTracking: String): Tracking?

    suspend fun actualizarTracking(tracking: Tracking)
}
