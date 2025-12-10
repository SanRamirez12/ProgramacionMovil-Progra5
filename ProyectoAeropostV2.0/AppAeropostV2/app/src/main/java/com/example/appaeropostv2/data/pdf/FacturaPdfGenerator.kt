package com.example.appaeropostv2.data.pdf

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.appaeropostv2.domain.logic.FacturaSecurityLogic
import com.example.appaeropostv2.domain.model.FacturaConDetalle
import com.example.appaeropostv2.interfaces.InterfacePdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Implementación Android de InterfacePdfGenerator SIN WebView.
 *
 * - Crea un PdfDocument directamente.
 * - Dibuja texto en el canvas (título, datos, totales, sello).
 * - Guarda el archivo en /Android/data/.../files/Documents/facturas
 * - Devuelve un Uri vía FileProvider.
 */
class FacturaPdfGenerator(
    private val context: Context
) : InterfacePdfGenerator {

    override suspend fun generarFacturaPDF(detalle: FacturaConDetalle): Uri =
        withContext(Dispatchers.IO) {
            // ================================
            // Preparar datos
            // ================================
            val factura = detalle.factura
            val cliente = detalle.cliente
            val paquete = detalle.paquete

            val claveSello = "${factura.numeroTracking}${factura.fechaFacturacion}"
            val selloDigital = FacturaSecurityLogic.generarSelloDigital(claveSello)

            // ================================
            // Crear documento PDF
            // ================================
            val pageWidth = 1240
            val pageHeight = 1754

            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(
                pageWidth,
                pageHeight,
                1
            ).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas

            // ================================
            // Configurar "pinceles" de texto
            // ================================
            val titlePaint = Paint().apply {
                isAntiAlias = true
                textSize = 40f
                typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            }

            val sectionPaint = Paint().apply {
                isAntiAlias = true
                textSize = 26f
                typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            }

            val textPaint = Paint().apply {
                isAntiAlias = true
                textSize = 22f
                typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            }

            val boldText = Paint().apply {
                isAntiAlias = true
                textSize = 22f
                typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            }

            // ================================
            // Layout básico (márgenes)
            // ================================
            val marginLeft = 80f
            var y = 120f
            val lineSpacing = 32f

            // ================================
            // Título
            // ================================
            val title = "FACTURA OFICIAL"
            val titleWidth = titlePaint.measureText(title)
            val centerX = (pageWidth - titleWidth) / 2f
            canvas.drawText(title, centerX, y, titlePaint)
            y += lineSpacing * 2

            // ================================
            // Datos generales
            // ================================
            canvas.drawText("Factura #${factura.idFacturacion}", marginLeft, y, boldText)
            y += lineSpacing
            canvas.drawText("Fecha facturación: ${factura.fechaFacturacion}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Tracking: ${factura.numeroTracking}", marginLeft, y, textPaint)
            y += lineSpacing * 2

            // ================================
            // Sección: Cliente
            // ================================
            canvas.drawText("Datos del cliente", marginLeft, y, sectionPaint)
            y += lineSpacing

            canvas.drawText("Nombre       : ${cliente.nombreCliente}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Cédula       : ${cliente.cedulaCliente}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Teléfono     : ${cliente.telefonoCliente}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Correo       : ${cliente.correoCliente}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Dirección    : ${factura.direccionEntrega}", marginLeft, y, textPaint)
            y += lineSpacing * 2

            // ================================
            // Sección: Paquete
            // ================================
            canvas.drawText("Datos del paquete", marginLeft, y, sectionPaint)
            y += lineSpacing

            canvas.drawText("Peso (kg)    : ${factura.pesoPaquete}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Valor bruto  : ${factura.valorBrutoPaquete}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Origen       : ${paquete.tiendaOrigen}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText("Casillero    : ${paquete.casillero}", marginLeft, y, textPaint)
            y += lineSpacing
            canvas.drawText(
                "Prod. especial: ${if (factura.productoEspecial) "Sí" else "No"}",
                marginLeft,
                y,
                textPaint
            )
            y += lineSpacing * 2

            // ================================
            // Sección: Cálculo de montos
            // ================================
            canvas.drawText("Detalle de cargos", marginLeft, y, sectionPaint)
            y += lineSpacing

            val cargoPeso = factura.pesoPaquete * 12
            val cargoImpuesto = factura.valorBrutoPaquete * 0.13
            val cargoEspecial = if (factura.productoEspecial) {
                factura.valorBrutoPaquete * 0.10
            } else {
                factura.valorBrutoPaquete * 0.05
            }

            canvas.drawText(
                "Peso x 12         : ${"%.2f".format(cargoPeso)}",
                marginLeft,
                y,
                textPaint
            )
            y += lineSpacing
            canvas.drawText(
                "Impuesto 13%      : ${"%.2f".format(cargoImpuesto)}",
                marginLeft,
                y,
                textPaint
            )
            y += lineSpacing
            canvas.drawText(
                "Recargo especial  : ${"%.2f".format(cargoEspecial)}",
                marginLeft,
                y,
                textPaint
            )
            y += lineSpacing * 2

            // ================================
            // Total
            // ================================
            canvas.drawText(
                "MONTO TOTAL : ${"%.2f".format(factura.montoTotal)}",
                marginLeft,
                y,
                boldText
            )
            y += lineSpacing * 3

            // ================================
            // Sello digital
            // ================================
            canvas.drawText("Sello digital:", marginLeft, y, sectionPaint)
            y += lineSpacing

            canvas.drawText(selloDigital, marginLeft, y, textPaint)
            y += lineSpacing * 2

            canvas.drawText(
                "Documento generado electrónicamente por AeropostApp.",
                marginLeft,
                y,
                textPaint
            )

            // Terminar página
            document.finishPage(page)

            // ================================
            // Guardar archivo
            // ================================
            val directory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "facturas"
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val fileName = "factura_${factura.idFacturacion}_${System.currentTimeMillis()}.pdf"
            val archivo = File(directory, fileName)

            archivo.outputStream().use { output ->
                document.writeTo(output)
            }
            document.close()

            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                archivo
            )

            uri
        }
}
