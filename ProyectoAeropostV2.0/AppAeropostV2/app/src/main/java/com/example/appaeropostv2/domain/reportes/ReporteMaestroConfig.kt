package com.example.appaeropostv2.domain.reportes

data class ReporteMaestroConfig(
    val incluirResumenEjecutivo: Boolean = true,
    val incluirClientes: Boolean = true,
    val incluirPaquetes: Boolean = true,
    val incluirFacturacion: Boolean = true,
    val incluirEstadisticasBasicas: Boolean = true
)
