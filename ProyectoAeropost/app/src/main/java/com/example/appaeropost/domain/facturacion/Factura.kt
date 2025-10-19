package com.example.appaeropost.domain


data class Factura(
    val id: String = java.util.UUID.randomUUID().toString(),
    val tracking: String,
    val cedulaCliente: String,
    val pesoKg: Double,
    val valorTotalPaquete: Double,
    val productoEspecial: Boolean,
    val fechaEntrega: String, // ISO yyyy-MM-dd para simplificar
    val montoTotal: Double,
    val direccionEntrega: String
)