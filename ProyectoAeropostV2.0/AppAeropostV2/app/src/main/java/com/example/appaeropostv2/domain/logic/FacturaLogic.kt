package com.example.appaeropostv2.domain.logic

object FacturaLogic {

    /**
     * Fórmula oficial:
     * Monto total = (peso × 12) + (valor × 13%) + (valor × comisión)
     * Comisión: 5% normal ó 10% si productoEspecial = true
     */
    fun calcularMontoTotal(
        pesoPaquete: Double,
        valorBruto: Double,
        productoEspecial: Boolean
    ): Double {

        val impuesto = valorBruto * 0.13
        val comision = valorBruto * if (productoEspecial) 0.10 else 0.05

        return (pesoPaquete * 12.0) + impuesto + comision
    }
}
