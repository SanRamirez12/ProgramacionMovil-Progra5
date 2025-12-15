package com.example.appaeropostv2.domain.logic

import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.TrackingStatus
import kotlin.math.max
import kotlin.math.min

object TrackingSimulationLogic {

    data class Punto(val latitud: Double, val longitud: Double)

    private const val INTERVALO_MS = 30_000L

    fun debeActualizar(ultimaActualizacionMillis: Long): Boolean {
        return (System.currentTimeMillis() - ultimaActualizacionMillis) >= INTERVALO_MS
    }

    fun estadoPorTiempo(
        tiempoInicioMillis: Long,
        casilleroOrigen: Casilleros
    ): TrackingStatus {

        val diasTranscurridos =
            (System.currentTimeMillis() - tiempoInicioMillis) / (1000L * 60L * 60L * 24L)

        val diasEmpaque = when (casilleroOrigen) {
            Casilleros.MIA -> 3
            Casilleros.LAS -> 4
            Casilleros.NYK -> 5
        }

        val diasAbordandoVuelo = 1

        val diasEnvio = when (casilleroOrigen) {
            Casilleros.MIA -> 10
            Casilleros.LAS -> 11
            Casilleros.NYK -> 12
        }

        val diasLlegandoOficinas = 1

        return when {
            diasTranscurridos < 1 -> TrackingStatus.ORDERED
            diasTranscurridos < diasEmpaque -> TrackingStatus.PACKED
            diasTranscurridos < (diasEmpaque + diasAbordandoVuelo + diasEnvio + diasLlegandoOficinas) ->
                TrackingStatus.IN_TRANSIT
            else -> TrackingStatus.DELIVERED
        }
    }

    /**
     * Movimiento simulado:
     * - ORDERED / PACKED: se queda en origen
     * - IN_TRANSIT: se mueve de origen -> SJO -> ULatina
     * - DELIVERED: se queda en ULatina
     */
    fun siguientePosicion(
        estado: TrackingStatus,
        casilleroOrigen: Casilleros,
        tiempoInicioMillis: Long
    ): Punto {

        val origen = when (casilleroOrigen) {
            Casilleros.MIA -> CoordinatesLogic.MIAMI
            Casilleros.LAS -> CoordinatesLogic.LOS_ANGELES
            Casilleros.NYK -> CoordinatesLogic.NEW_YORK
        }

        val sjo = CoordinatesLogic.JUAN_SANTAMARIA
        val central = CoordinatesLogic.UNIVERSIDAD_LATINA

        if (estado == TrackingStatus.ORDERED || estado == TrackingStatus.PACKED) {
            return Punto(origen.latitud, origen.longitud)
        }

        if (estado == TrackingStatus.DELIVERED) {
            return Punto(central.latitud, central.longitud)
        }

        // IN_TRANSIT: progreso continuo 0..1 basado en tiempo
        val horasTranscurridas =
            (System.currentTimeMillis() - tiempoInicioMillis).toDouble() / (1000.0 * 60.0 * 60.0)

        val horasVentana = 14.0 * 24.0
        val progreso = max(0.0, min(1.0, horasTranscurridas / horasVentana))

        return if (progreso <= 0.5) {
            val p = progreso / 0.5
            interpolar(origen.latitud, origen.longitud, sjo.latitud, sjo.longitud, p)
        } else {
            val p = (progreso - 0.5) / 0.5
            interpolar(sjo.latitud, sjo.longitud, central.latitud, central.longitud, p)
        }
    }

    private fun interpolar(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double,
        t: Double
    ): Punto {
        val tt = max(0.0, min(1.0, t))
        val lat = lat1 + (lat2 - lat1) * tt
        val lon = lon1 + (lon2 - lon1) * tt
        return Punto(lat, lon)
    }
}
