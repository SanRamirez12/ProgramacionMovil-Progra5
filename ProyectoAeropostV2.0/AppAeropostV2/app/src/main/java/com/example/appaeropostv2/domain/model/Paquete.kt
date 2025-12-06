package com.example.appaeropostv2.domain.model

import com.example.appaeropostv2.domain.enums.Casilleros
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.enums.Tiendas
import java.math.BigDecimal
import java.time.LocalDate

data class Paquete(
    val idPaquete: String,
    val fechaRegistro: LocalDate,
    val idCliente: Int,
    val nombrecliente: String,
    val cedulaCliente: String,
    val numeroTracking: String,
    val pesoPaquete: Double,
    val valorBruto: BigDecimal,
    val monedasPaquete: Monedas,
    val tiendaOrigen: Tiendas,
    val casillero: Casilleros,
    val condicionEspecial: Boolean
)
