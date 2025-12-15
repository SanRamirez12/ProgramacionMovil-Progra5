package com.example.appaeropostv2.data.local.dto

/**
 * DTOs para queries agregadas de reportes.
 * Room puede mapear SELECT ... AS campo a data class automáticamente.
 */

data class ClientesPorTipoDto(
    val tipoCliente: String,
    val cantidad: Int
)

data class PaquetesPorTiendaDto(
    val tiendaOrigen: String,
    val cantidad: Int,
    val pesoTotal: Double,
    val pesoPromedio: Double
)

data class PaquetesEspecialesDto(
    val esEspecial: Int, // 1/0
    val cantidad: Int,
    val porcentaje: Double
)

data class FacturacionPorMesDto(
    val anioMes: String, // "YYYY-MM"
    val totalMonto: Double,
    val cantidadFacturas: Int,
    val ticketPromedio: Double
)

data class FacturacionPorClienteDto(
    val cedulaCliente: String,
    val totalMonto: Double,
    val cantidadFacturas: Int,
    val ticketPromedio: Double
)

data class FacturacionPorTrackingDto(
    val numeroTracking: String,
    val cedulaCliente: String,
    val fechaFacturacion: String,
    val montoTotal: Double
)
