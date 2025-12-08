package com.example.appaeropostv2.domain.pdf

import android.net.Uri
import com.example.appaeropostv2.domain.model.FacturaConDetalle

interface FacturaPdfGenerator {
    suspend fun generarFacturaPDF(detalle: FacturaConDetalle): Uri
}
