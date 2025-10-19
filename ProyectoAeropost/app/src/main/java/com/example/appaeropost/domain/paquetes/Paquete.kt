package com.example.appaeropost.domain.paquetes

import java.math.BigDecimal
import java.time.LocalDate

// Enums top-level: fáciles de importar y refactorizar luego si se reutilizan.
enum class Moneda { USD, CRC, EUR }

enum class TiendaOrigen {
    AMAZON, EBAY, ALIEXPRESS, TEMU, SHEIN, OTRO
}

data class Paquete(
    val id: String,
    val fechaRegistro: LocalDate,
    val clienteId: String,
    val clienteNombre: String,
    val clienteCedula: String,
    val tracking: String,
    val pesoLb: Double,
    val valorBruto: BigDecimal,
    val moneda: Moneda,
    val tiendaOrigen: TiendaOrigen,
    val condicionEspecial: Boolean
)
