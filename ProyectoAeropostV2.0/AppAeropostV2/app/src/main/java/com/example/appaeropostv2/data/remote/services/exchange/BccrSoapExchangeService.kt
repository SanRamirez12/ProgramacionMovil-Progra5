package com.example.appaeropostv2.data.remote.services.exchange

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Servicio SOAP para consultar el tipo de cambio del BCCR.
 *
 * Indicadores usados (OFICIALES):
 *  - USD compra → 317
 *  - EUR compra → 333
 *
 * Para efectos del proyecto:
 *  - Credenciales (correo + token) se dejan hardcodeadas.
 *  - Si el WS falla, el RepositoryExchange se encarga del fallback.
 */
class BccrSoapExchangeService(
    private val client: OkHttpClient = OkHttpClient()
) {

    companion object {
        // Endpoint oficial del BCCR
        private const val ENDPOINT =
            "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx"

        // SOAP Action requerida por el WS
        private const val SOAP_ACTION =
            "http://ws.sdde.bccr.fi.cr/ObtenerIndicadoresEconomicos"

        // Indicadores de tipo de cambio (COMPRA)
        const val INDICADOR_USD_COMPRA = 317
        const val INDICADOR_EUR_COMPRA = 333

        // ====== CREDENCIALES BCCR ======
        private const val BCCR_EMAIL = "santiago.ramirez3@ulatina.net"
        private const val BCCR_TOKEN = "TTOADGIZAI"
        private const val BCCR_NOMBRE = "AeropostApp"
        // ===============================

        private const val SUBNIVELES = "N"

        // El WS suele exigir este formato
        private val DATE_FORMAT: DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }

    /**
     * Obtiene el valor NUM_VALOR del indicador para una fecha.
     *
     * @param indicador 317 (USD compra) o 333 (EUR compra)
     * @param date fecha a consultar
     * @return valor del tipo de cambio o null si falla
     */
    fun getIndicadorValor(
        indicador: Int,
        date: LocalDate
    ): Double? {

        val fecha = date.format(DATE_FORMAT)

        val soapEnvelope = """
            <?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                           xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                           xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
              <soap:Body>
                <ObtenerIndicadoresEconomicos xmlns="http://ws.sdde.bccr.fi.cr">
                  <Indicador>$indicador</Indicador>
                  <FechaInicio>$fecha</FechaInicio>
                  <FechaFinal>$fecha</FechaFinal>
                  <Nombre>$BCCR_NOMBRE</Nombre>
                  <SubNiveles>$SUBNIVELES</SubNiveles>
                  <CorreoElectronico>$BCCR_EMAIL</CorreoElectronico>
                  <Token>$BCCR_TOKEN</Token>
                </ObtenerIndicadoresEconomicos>
              </soap:Body>
            </soap:Envelope>
        """.trimIndent()

        val body = soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(ENDPOINT)
            .post(body)
            .addHeader("Content-Type", "text/xml; charset=utf-8")
            .addHeader("SOAPAction", SOAP_ACTION)
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null
                val xml = response.body?.string() ?: return null
                parseSoapForNumValor(xml)
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Extrae el primer <NUM_VALOR> del XML devuelto por el BCCR.
     */
    private fun parseSoapForNumValor(xml: String): Double? {
        return try {
            val dbf = DocumentBuilderFactory.newInstance().apply {
                isNamespaceAware = true
            }
            val db = dbf.newDocumentBuilder()
            val doc = db.parse(InputSource(StringReader(xml)))

            val nodes = doc.getElementsByTagName("NUM_VALOR")
            if (nodes.length == 0) return null

            val text = (nodes.item(0) as? Element)?.textContent?.trim()
                ?: return null

            // Por si viene con coma decimal
            text.replace(",", ".").toDoubleOrNull()
        } catch (_: Exception) {
            null
        }
    }
}
