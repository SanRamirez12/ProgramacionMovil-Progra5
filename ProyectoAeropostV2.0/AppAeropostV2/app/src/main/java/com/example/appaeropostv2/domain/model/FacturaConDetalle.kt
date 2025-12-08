package com.example.appaeropostv2.domain.model

data class FacturaConDetalle(
    val factura: Facturacion,
    val cliente: Cliente,
    val paquete: Paquete
)
