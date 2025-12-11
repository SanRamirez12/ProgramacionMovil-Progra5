package com.example.appaeropostv2.domain.model

/**
 * Datos ya procesados para generar el PDF.
 * Aquí no hay lógica Android, solo info "lista para dibujar".
 */
data class FacturaPdfData(
    val factura: Facturacion,
    val cliente: Cliente,
    val paquete: Paquete,
    val cargoPeso: Double,
    val cargoImpuesto: Double,
    val cargoEspecial: Double,
    val selloDigital: String
)
