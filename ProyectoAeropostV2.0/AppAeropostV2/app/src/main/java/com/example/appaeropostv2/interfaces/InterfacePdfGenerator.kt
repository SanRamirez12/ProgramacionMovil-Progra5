package com.example.appaeropostv2.interfaces

import android.net.Uri
import com.example.appaeropostv2.domain.model.FacturaConDetalle

interface InterfacePdfGenerator {
    suspend fun generarFacturaPDF(detalle: FacturaConDetalle): Uri
}