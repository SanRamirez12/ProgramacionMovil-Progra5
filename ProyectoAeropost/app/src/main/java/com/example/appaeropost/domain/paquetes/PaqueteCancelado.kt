package com.example.appaeropost.domain.paquetes

import java.math.BigDecimal
import java.time.LocalDate

/**
 * Historial de cancelación de un Paquete (soft-delete).
 * Guarda una copia de los datos clave + motivo y fecha de cancelación.
 */
data class PaqueteCancelado(
    val paqueteId: String,
    val fechaRegistro: LocalDate,
    val clienteId: String,
    val clienteNombre: String,
    val clienteCedula: String,
    val tracking: String,
    val pesoLb: Double,
    val valorBruto: BigDecimal,
    val moneda: Moneda,
    val tiendaOrigen: TiendaOrigen,
    val condicionEspecial: Boolean,
    val motivo: String,
    val fechaCancelacion: LocalDate
)
