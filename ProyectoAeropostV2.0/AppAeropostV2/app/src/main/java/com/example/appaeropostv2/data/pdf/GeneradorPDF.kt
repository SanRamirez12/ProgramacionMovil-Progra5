package com.example.appaeropostv2.data.pdf

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.FileProvider
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.FacturaConDetalle
import com.example.appaeropostv2.domain.pdf.FacturaPdfGenerator
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import kotlin.coroutines.resume

class AndroidFacturaPdfGenerator(
    private val context: Context
) : FacturaPdfGenerator {

    override suspend fun generarFacturaPDF(detalle: FacturaConDetalle): Uri =
        suspendCancellableCoroutine { continuation ->

            val webView = WebView(context)
            webView.settings.defaultTextEncodingName = "utf-8"

            val html = buildHtml(detalle)

            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (view == null) return

                    val pageWidth = 1240
                    val pageHeight = 1754

                    val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        pageWidth,
                        View.MeasureSpec.EXACTLY
                    )
                    val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        pageHeight,
                        View.MeasureSpec.EXACTLY
                    )

                    view.measure(widthMeasureSpec, heightMeasureSpec)
                    view.layout(0, 0, pageWidth, pageHeight)

                    val doc = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
                    val page = doc.startPage(pageInfo)

                    view.draw(page.canvas)
                    doc.finishPage(page)

                    val directory =
                        File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "facturas")
                    if (!directory.exists()) directory.mkdirs()

                    val archivo = File(directory, "factura_${detalle.factura.idFacturacion}.pdf")
                    val output = FileOutputStream(archivo)
                    doc.writeTo(output)
                    doc.close()
                    output.close()

                    val uri = FileProvider.getUriForFile(
                        context,
                        context.packageName + ".provider",
                        archivo
                    )

                    continuation.resume(uri)
                }
            }

            webView.loadDataWithBaseURL(
                null,
                html,
                "text/html",
                "utf-8",
                null
            )
        }

    // -------------------------------------------------------------
    // HTML TEMPLATE + CSS estilo comprobante oficial
    // -------------------------------------------------------------
    private fun buildHtml(det: FacturaConDetalle): String {

        val factura = det.factura
        val cliente = det.cliente
        val paquete = det.paquete

        val hash = generarSelloDigital("${factura.numeroTracking}${factura.fechaFacturacion}")

        val symbol = when (paquete.monedasPaquete) {
            Monedas.USD -> "$"
            Monedas.CRC -> "₡"
            Monedas.EUR -> "€"
        }

        return """
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<style>

body {
    font-family: Arial, sans-serif;
    padding: 20px;
}

.contenedor {
    border: 2px solid #000;
    padding: 20px;
}

h1 {
    text-align: center;
    margin: 0;
    font-size: 26px;
    padding-bottom: 10px;
}

h2 {
    border-bottom: 1px solid #333;
    padding-bottom: 4px;
    margin-top: 25px;
    font-size: 18px;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

td, th {
    border: 1px solid #444;
    padding: 6px;
    font-size: 14px;
}

.total {
    font-size: 20px;
    font-weight: bold;
    text-align: right;
    margin-top: 15px;
}

.sello {
    margin-top: 25px;
    font-size: 12px;
    text-align: center;
    color: #555;
}

</style>
</head>

<body>
<div class="contenedor">

<h1>FACTURA OFICIAL</h1>

<h2>Datos del Cliente</h2>
<table>
<tr><td><b>Nombre:</b></td><td>${cliente.nombreCliente}</td></tr>
<tr><td><b>Cédula:</b></td><td>${cliente.cedulaCliente}</td></tr>
<tr><td><b>Teléfono:</b></td><td>${cliente.telefonoCliente}</td></tr>
<tr><td><b>Correo:</b></td><td>${cliente.correoCliente}</td></tr>
<tr><td><b>Dirección entrega:</b></td><td>${factura.direccionEntrega}</td></tr>
</table>

<h2>Datos del Paquete</h2>
<table>
<tr><td><b>Tracking:</b></td><td>${factura.numeroTracking}</td></tr>
<tr><td><b>Peso:</b></td><td>${factura.pesoPaquete} kg</td></tr>
<tr><td><b>Valor declarado:</b></td><td>$symbol${String.format("%.2f", factura.valorBrutoPaquete)}</td></tr>
<tr><td><b>Fecha registro:</b></td><td>${paquete.fechaRegistro}</td></tr>
<tr><td><b>Origen:</b></td><td>${paquete.tiendaOrigen} / ${paquete.casillero}</td></tr>
<tr><td><b>Condición especial:</b></td><td>${if (factura.productoEspecial) "Sí" else "No"}</td></tr>
</table>

<h2>Detalle de Cargos</h2>
<table>
<tr><th>Concepto</th><th>Monto</th></tr>
<tr><td>Peso × 12</td><td>$symbol${String.format("%.2f", factura.pesoPaquete * 12)}</td></tr>
<tr><td>Impuesto 13%</td><td>$symbol${String.format("%.2f", factura.valorBrutoPaquete * 0.13)}</td></tr>
<tr><td>Comisión (${if (factura.productoEspecial) "10%" else "5%"})</td><td>$symbol${
            String.format(
                "%.2f",
                if (factura.productoEspecial)
                    factura.valorBrutoPaquete * 0.10
                else
                    factura.valorBrutoPaquete * 0.05
            )
        }</td></tr>
</table>

<div class="total">
TOTAL A PAGAR: $symbol${String.format("%.2f", factura.montoTotal)}
</div>

<div class="sello">
Sello digital: $hash<br>
Documento generado electrónicamente por AeropostApp.
</div>

</div>
</body>
</html>
        """.trimIndent()
    }

    // -------------------------------------------------------------
    // Generación del sello digital simulado
    // -------------------------------------------------------------
    private fun generarSelloDigital(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray())
        return Base64.encodeToString(digest, Base64.NO_WRAP).take(32)
    }
}
