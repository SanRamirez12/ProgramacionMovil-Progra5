package com.example.appaeropostv2.interfaces

import android.net.Uri
import com.example.appaeropostv2.domain.model.FacturaPdfData

/**
 * Contrato para cualquier generador de PDF de facturas.
 * Presentation / ViewModel solo conoce esta interfaz, no la implementación.
 */
interface InterfacePdfGenerator {
    suspend fun generarFacturaPDF(datos: FacturaPdfData): Uri
}
