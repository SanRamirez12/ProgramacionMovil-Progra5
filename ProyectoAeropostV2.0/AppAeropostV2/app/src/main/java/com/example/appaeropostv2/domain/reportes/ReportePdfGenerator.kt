package com.example.appaeropostv2.data.reportes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.appaeropostv2.data.local.dto.ClientesPorTipoDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorMesDto
import com.example.appaeropostv2.data.local.dto.PaquetesPorTiendaDto
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.domain.model.ExchangeRates
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.domain.model.Paquete
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

class ReportePdfGenerator(
    private val context: Context
) {
    fun generar(
        titulo: String,
        fecha: LocalDate,
        rates: ExchangeRates,
        clientes: List<Cliente>,
        paquetes: List<Paquete>,
        facturas: List<Facturacion>,
        resumen: List<String>,

        // agregados
        clientesPorTipo: List<ClientesPorTipoDto> = emptyList(),
        paquetesPorTienda: List<PaquetesPorTiendaDto> = emptyList(),
        facturacionPorMes: List<FacturacionPorMesDto> = emptyList()
    ): Uri {
        val doc = PdfDocument()
        val paint = Paint().apply { textSize = 12f }
        val paintTitle = Paint().apply { textSize = 18f; isFakeBoldText = true }

        fun newPage(pageNumber: Int): Pair<PdfDocument.Page, Canvas> {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            val page = doc.startPage(pageInfo)
            return page to page.canvas
        }

        var pageNum = 1
        var (page, canvas) = newPage(pageNum)
        var y = 60f

        fun line(text: String, size: Float = 12f, bold: Boolean = false) {
            paint.textSize = size
            paint.isFakeBoldText = bold
            canvas.drawText(text, 40f, y, paint)
            y += (size + 8f)
            if (y > 800f) {
                doc.finishPage(page)
                pageNum++
                val p = newPage(pageNum)
                page = p.first
                canvas = p.second
                y = 60f
            }
        }

        canvas.drawText(titulo, 40f, 40f, paintTitle)

        line("Fecha de generación: $fecha")
        line("Tipo de cambio usado (Compra):", 12f, true)
        line("• USD→CRC: ${rates.usdCompraToCrc}   • EUR→CRC: ${rates.eurCompraToCrc}   (Fuente: ${rates.source})")

        y += 10f
        line("Resumen ejecutivo", 14f, true)
        resumen.forEach { line("• $it") }

        y += 10f
        line("Clientes", 14f, true)
        line("Total clientes: ${clientes.size}")
        clientesPorTipo.take(6).forEach { r -> line("• ${r.tipoCliente}: ${r.cantidad}") }

        y += 10f
        line("Paquetes", 14f, true)
        line("Total paquetes: ${paquetes.size}")
        paquetesPorTienda.take(6).forEach { r -> line("• ${r.tiendaOrigen}: ${r.cantidad} (peso prom: ${"%.2f".format(r.pesoPromedio)})") }

        y += 10f
        line("Facturación", 14f, true)
        line("Total facturas: ${facturas.size}")
        facturacionPorMes.take(6).forEach { r -> line("• ${r.anioMes}: total=${"%.2f".format(r.totalMonto)} | #=${r.cantidadFacturas}") }

        doc.finishPage(page)

        val outFile = File(context.cacheDir, "ReporteMaestro_Aeropost.pdf")
        FileOutputStream(outFile).use { fos -> doc.writeTo(fos) }
        doc.close()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            outFile
        )
    }
}
