package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoTracking
import com.example.appaeropostv2.data.local.entity.EntityTracking
import com.example.appaeropostv2.data.local.mappers.toDomain
import com.example.appaeropostv2.data.local.mappers.toEntity
import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.logic.CoordinatesLogic
import com.example.appaeropostv2.domain.logic.TrackingSimulationLogic
import com.example.appaeropostv2.domain.model.Tracking
import com.example.appaeropostv2.interfaces.InterfaceTracking

class RepositoryTracking(
    private val daoTracking: DaoTracking
) : InterfaceTracking {

    override suspend fun crearTracking(
        idPaquete: String,
        numeroTracking: String,
        casilleroOrigen: Casilleros,
        tiempoInicioMillis: Long
    ) {
        val ahora = System.currentTimeMillis()

        // Estado inicial correcto según fecha del paquete
        val estadoInicial = TrackingSimulationLogic.estadoPorTiempo(
            tiempoInicioMillis = tiempoInicioMillis,
            casilleroOrigen = casilleroOrigen
        )

        // Posición inicial correcta según estado (si ya está DELIVERED → ULatina)
        val posInicial = TrackingSimulationLogic.siguientePosicion(
            estado = estadoInicial,
            casilleroOrigen = casilleroOrigen,
            tiempoInicioMillis = tiempoInicioMillis
        )

        val entity = EntityTracking(
            idPaquete = idPaquete,
            numeroTracking = numeroTracking,
            casilleroOrigen = casilleroOrigen,
            estado = estadoInicial,
            latitud = posInicial.latitud,
            longitud = posInicial.longitud,
            tiempoInicioMillis = tiempoInicioMillis,
            ultimaActualizacionMillis = ahora
        )

        daoTracking.insertar(entity)
    }


    override suspend fun obtenerTrackingsPorCedula(cedula: String): List<Tracking> =
        daoTracking.obtenerPorCedulaCliente(cedula).map { it.toDomain() }

    override suspend fun obtenerTrackingPorNumero(numeroTracking: String): Tracking? =
        daoTracking.obtenerPorNumeroTracking(numeroTracking)?.toDomain()

    override suspend fun actualizarTracking(tracking: Tracking) {
        daoTracking.actualizar(tracking.toEntity())
    }
}
