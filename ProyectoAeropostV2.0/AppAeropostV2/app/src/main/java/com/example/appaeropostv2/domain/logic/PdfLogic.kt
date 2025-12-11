package com.example.appaeropostv2.domain.logic

import com.example.appaeropostv2.domain.model.FacturaConDetalle
import com.example.appaeropostv2.domain.model.FacturaPdfData

/**
 * Lógica de negocio relacionada con el PDF de la factura:
 * cálculos de cargos y generación del sello digital.
 */
object PdfLogic {

    fun buildFacturaPdfData(detalle: FacturaConDetalle): FacturaPdfData {
        val factura = detalle.factura
        val cliente = detalle.cliente
        val paquete = detalle.paquete

        // Cálculos de negocio (antes estaban en el generador)
        val cargoPeso = factura.pesoPaquete * 12
        val cargoImpuesto = factura.valorBrutoPaquete * 0.13
        val cargoEspecial =
            if (factura.productoEspecial) {
                factura.valorBrutoPaquete * 0.10
            } else {
                factura.valorBrutoPaquete * 0.05
            }

        // Sello digital desde lógica de dominio
        val selloDigital = FacturaSecurityLogic.generarSelloDigital(
            "${factura.numeroTracking}${factura.fechaFacturacion}"
        )

        return FacturaPdfData(
            factura = factura,
            cliente = cliente,
            paquete = paquete,
            cargoPeso = cargoPeso,
            cargoImpuesto = cargoImpuesto,
            cargoEspecial = cargoEspecial,
            selloDigital = selloDigital
        )
    }
}
