package com.example.appaeropostv2.domain.reportes

data class ClientesPorTipoRow(
    val tipoCliente: String,
    val cantidad: Int
)

data class PaquetesPorTiendaRow(
    val tiendaOrigen: String,
    val cantidad: Int,
    val pesoTotal: Double,
    val pesoPromedio: Double
)

data class PaquetesEspecialesRow(
    val esEspecial: Boolean,
    val cantidad: Int,
    val porcentaje: Double
)

data class FacturacionPorMesRow(
    val anioMes: String,
    val totalMonto: Double,
    val cantidadFacturas: Int,
    val ticketPromedio: Double
)

data class FacturacionPorClienteRow(
    val cedulaCliente: String,
    val totalMonto: Double,
    val cantidadFacturas: Int,
    val ticketPromedio: Double
)

data class FacturacionPorTrackingRow(
    val numeroTracking: String,
    val cedulaCliente: String,
    val fechaFacturacion: String,
    val montoTotal: Double
)
