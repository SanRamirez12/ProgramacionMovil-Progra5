package com.example.appaeropostv2.interfaces

import android.net.Uri
import com.example.appaeropostv2.domain.reportes.ReporteMaestroConfig

interface InterfaceReportes {
    suspend fun generarPdfReporteMaestro(config: ReporteMaestroConfig): Uri
    suspend fun generarXlsxReporteMaestro(config: ReporteMaestroConfig): Uri
    suspend fun enviarReporteMaestroPorCorreo(config: ReporteMaestroConfig): Boolean
}
