package com.example.appaeropostv2.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.appaeropostv2.core.session.SessionManager
import com.example.appaeropostv2.data.local.dao.DaoReporte
import com.example.appaeropostv2.data.local.entity.EntityReporte
import com.example.appaeropostv2.data.reportes.ReportePdfGenerator
import com.example.appaeropostv2.data.reportes.ReporteZipCsvGenerator
import com.example.appaeropostv2.domain.common.Resource
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.model.ExchangeRates
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.domain.reportes.ReporteMaestroConfig
import com.example.appaeropostv2.interfaces.InterfaceReportes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.math.roundToInt

class RepositoryReportes(
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val daoReporte: DaoReporte,
    private val repoCliente: RepositoryCliente,
    private val repoPaquete: RepositoryPaquete,
    private val repoFacturacion: RepositoryFacturacion,
    private val repoEmail: RepositoryEmail,
    private val repoExchange: RepositoryExchange
) : InterfaceReportes {

    private val pdfGen = ReportePdfGenerator(context)
    private val zipGen = ReporteZipCsvGenerator(context)

    override suspend fun generarPdfReporteMaestro(config: ReporteMaestroConfig): Uri = withContext(Dispatchers.IO) {
        val fecha = LocalDate.now()
        val rates = repoExchange.getRatesCompraForDate(fecha)

        val clientes = if (config.incluirClientes) repoCliente.buscar("") else emptyList()
        val paquetes = if (config.incluirPaquetes) repoPaquete.buscar("") else emptyList()
        val facturas = if (config.incluirFacturacion) repoFacturacion.buscar("") else emptyList()

        val clientesPorTipo = if (config.incluirClientes) daoReporte.clientesPorTipo() else emptyList()
        val paquetesPorTienda = if (config.incluirPaquetes) daoReporte.paquetesPorTienda() else emptyList()
        val facturacionPorMes = if (config.incluirFacturacion) daoReporte.facturacionPorMes() else emptyList()

        val resumen = buildResumen(config, rates, clientes.size, paquetes.size, facturas.size)

        pdfGen.generar(
            titulo = "Reporte Maestro Aeropost",
            fecha = fecha,
            rates = rates,
            clientes = clientes,
            paquetes = paquetes,
            facturas = facturas,
            resumen = resumen,
            clientesPorTipo = clientesPorTipo,
            paquetesPorTienda = paquetesPorTienda,
            facturacionPorMes = facturacionPorMes
        )
    }

    override suspend fun generarXlsxReporteMaestro(config: ReporteMaestroConfig): Uri = withContext(Dispatchers.IO) {
        val fecha = LocalDate.now()
        val rates = repoExchange.getRatesCompraForDate(fecha)

        val clientes = if (config.incluirClientes) repoCliente.buscar("") else emptyList()
        val paquetes = if (config.incluirPaquetes) repoPaquete.buscar("") else emptyList()
        val facturas = if (config.incluirFacturacion) repoFacturacion.buscar("") else emptyList()

        val clientesPorTipo = if (config.incluirClientes) daoReporte.clientesPorTipo() else emptyList()
        val paquetesPorTienda = if (config.incluirPaquetes) daoReporte.paquetesPorTienda() else emptyList()
        val paquetesEspeciales = if (config.incluirPaquetes) daoReporte.paquetesEspecialesVsNormales() else emptyList()

        val facturacionPorMes = if (config.incluirFacturacion) daoReporte.facturacionPorMes() else emptyList()
        val facturacionPorCliente = if (config.incluirFacturacion) daoReporte.facturacionPorCliente() else emptyList()
        val facturacionPorTracking = if (config.incluirFacturacion) daoReporte.facturacionPorTracking() else emptyList()

        val estadisticas = if (config.incluirEstadisticasBasicas) {
            buildEstadisticasBasicas(rates, paquetes, facturas)
        } else emptyMap()

        zipGen.generarZip(
            config = config,
            fecha = fecha,
            rates = rates,
            clientes = clientes,
            paquetes = paquetes,
            facturas = facturas,
            estadisticas = estadisticas,
            clientesPorTipo = clientesPorTipo,
            paquetesPorTienda = paquetesPorTienda,
            paquetesEspeciales = paquetesEspeciales,
            facturacionPorMes = facturacionPorMes,
            facturacionPorCliente = facturacionPorCliente,
            facturacionPorTracking = facturacionPorTracking
        )
    }

    override suspend fun enviarReporteMaestroPorCorreo(config: ReporteMaestroConfig): Boolean = withContext(Dispatchers.IO) {
        val usuario = SessionManager.currentUser.value ?: return@withContext false
        val to = usuario.correoUsuario ?: return@withContext false

        val fecha = LocalDate.now()
        val rates = repoExchange.getRatesCompraForDate(fecha)

        val pdfUri = generarPdfReporteMaestro(config)
        val zipUri = generarXlsxReporteMaestro(config) // ZIP CSV

        val flow = repoEmail.sendEmailWithAttachments(
            to = to,
            subject = "Reporte Maestro Aeropost",
            text = "Adjunto encontrarás el Reporte Maestro en PDF y un ZIP con CSVs (uno por sección).",
            attachments = listOf(
                RepositoryEmail.AttachmentInput(
                    uri = pdfUri,
                    filename = "ReporteMaestro_Aeropost.pdf",
                    mimeType = "application/pdf"
                ),
                RepositoryEmail.AttachmentInput(
                    uri = zipUri,
                    filename = "ReporteMaestro_Aeropost_CSV.zip",
                    mimeType = "application/zip"
                )
            )
        )

        val ok = when (flow.first { it !is Resource.Loading }) {
            is Resource.Success -> true
            else -> false
        }

        if (ok) {
            daoReporte.insertReporte(
                EntityReporte(
                    userId = usuario.idUsuario,
                    reporteNombre = "Reporte Maestro Aeropost",
                    generatedAtMillis = System.currentTimeMillis(),
                    configJson = config.toString(),
                    tcUsdCompra = rates.usdCompraToCrc,
                    tcEurCompra = rates.eurCompraToCrc,
                    tcSource = rates.source,
                    pdfUri = pdfUri.toString(),
                    xlsxUri = zipUri.toString()
                )
            )
        }

        ok
    }

    private fun buildResumen(
        config: ReporteMaestroConfig,
        rates: ExchangeRates,
        nClientes: Int,
        nPaquetes: Int,
        nFacturas: Int
    ): List<String> {
        val list = mutableListOf<String>()
        list += "Documento maestro unificado (PDF + ZIP de CSVs) generado por checkboxes."
        list += "Tipo de cambio (Compra) usado: USD→CRC=${rates.usdCompraToCrc}, EUR→CRC=${rates.eurCompraToCrc} (Fuente: ${rates.source})."
        if (config.incluirClientes) list += "Clientes incluidos: $nClientes"
        if (config.incluirPaquetes) list += "Paquetes incluidos: $nPaquetes"
        if (config.incluirFacturacion) list += "Facturación incluida: $nFacturas"
        return list
    }

    private fun buildEstadisticasBasicas(
        rates: ExchangeRates,
        paquetes: List<Paquete>,
        facturas: List<Facturacion>
    ): Map<String, Any> {
        val pesoProm = if (paquetes.isNotEmpty()) paquetes.map { it.pesoPaquete }.average() else 0.0

        val totalValorBrutoCrc = paquetes.sumOf { p ->
            val v = p.valorBruto.toDouble()
            when (p.monedasPaquete) {
                Monedas.CRC -> v
                Monedas.USD -> v * rates.usdCompraToCrc
                Monedas.EUR -> v * rates.eurCompraToCrc
            }
        }

        val totalFacturadoRaw = facturas.sumOf { it.montoTotal }

        return linkedMapOf(
            "Paquetes (count)" to paquetes.size,
            "Facturas (count)" to facturas.size,
            "Peso promedio (kg)" to ((pesoProm * 100.0).roundToInt() / 100.0),
            "Total valor bruto (CRC aprox)" to totalValorBrutoCrc.roundToInt(),
            "Total facturado (raw)" to totalFacturadoRaw.roundToInt()
        )
    }
}
