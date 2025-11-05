package com.example.appaeropostv2.domain.model

data class Facturacion(
    val idFacturacion: Int,
    val numeroTracking: String,
    val cedulaCliente: String,
    val pesoPaquete: Double,
    val valorBrutoPaquete: Double,
    val productoEspecial: Boolean,
    val fechaFacturacion: String, // ISO yyyy-MM-dd para simplificar
    val montoTotal: Double,
    val direccionEntrega: String
)