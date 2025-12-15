package com.example.appaeropostv2.data.reportes

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.appaeropostv2.data.local.dto.ClientesPorTipoDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorClienteDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorMesDto
import com.example.appaeropostv2.data.local.dto.FacturacionPorTrackingDto
import com.example.appaeropostv2.data.local.dto.PaquetesEspecialesDto
import com.example.appaeropostv2.data.local.dto.PaquetesPorTiendaDto
import com.example.appaeropostv2.domain.model.Cliente
import com.example.appaeropostv2.domain.model.ExchangeRates
import com.example.appaeropostv2.domain.model.Facturacion
import com.example.appaeropostv2.domain.model.Paquete
import com.example.appaeropostv2.domain.reportes.ReporteMaestroConfig
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ReporteZipCsvGenerator(
    private val context: Context
) {

    fun generarZip(
        config: ReporteMaestroConfig,
        fecha: LocalDate,
        rates: ExchangeRates,
        clientes: List<Cliente>,
        paquetes: List<Paquete>,
        facturas: List<Facturacion>,
        estadisticas: Map<String, Any>,

        // agregados
        clientesPorTipo: List<ClientesPorTipoDto> = emptyList(),
        paquetesPorTienda: List<PaquetesPorTiendaDto> = emptyList(),
        paquetesEspeciales: List<PaquetesEspecialesDto> = emptyList(),
        facturacionPorMes: List<FacturacionPorMesDto> = emptyList(),
        facturacionPorCliente: List<FacturacionPorClienteDto> = emptyList(),
        facturacionPorTracking: List<FacturacionPorTrackingDto> = emptyList()
    ): Uri {
        val workDir = File(context.cacheDir, "reporte_csv_tmp").apply {
            if (exists()) deleteRecursively()
            mkdirs()
        }

        // ✅ SIEMPRE: Tipo de cambio
        writeCsv(
            File(workDir, "TipoCambio.csv"),
            headers = listOf("Campo", "Valor"),
            rows = listOf(
                listOf("Fecha", fecha.toString()),
                listOf("USD_CRC_COMPRA", rates.usdCompraToCrc.toString()),
                listOf("EUR_CRC_COMPRA", rates.eurCompraToCrc.toString()),
                listOf("Fuente", rates.source)
            )
        )

        // ✅ SIEMPRE: Estadísticas (aunque venga vacío, igual se genera con header)
        writeCsv(
            File(workDir, "Estadisticas.csv"),
            headers = listOf("Metrica", "Valor"),
            rows = estadisticas.entries.map { listOf(it.key, it.value.toString()) }
        )

        // ✅ Condicional: Clientes
        if (config.incluirClientes) {
            writeCsv(
                File(workDir, "Clientes_Tipo.csv"),
                headers = listOf("Tipo", "Cantidad"),
                rows = clientesPorTipo.map { listOf(it.tipoCliente, it.cantidad.toString()) }
            )

            writeCsv(
                File(workDir, "Raw_Clientes.csv"),
                headers = listOf("Id", "Nombre", "Cedula", "Correo", "Telefono"),
                rows = clientes.map {
                    listOf(
                        it.idCliente.toString(),
                        it.nombreCliente,
                        it.cedulaCliente,
                        it.correoCliente,
                        it.telefonoCliente
                    )
                }
            )
        }

        // ✅ Condicional: Paquetes
        if (config.incluirPaquetes) {
            writeCsv(
                File(workDir, "Paquetes_Tienda.csv"),
                headers = listOf("Tienda", "Cantidad", "PesoTotal", "PesoPromedio"),
                rows = paquetesPorTienda.map {
                    listOf(
                        it.tiendaOrigen,
                        it.cantidad.toString(),
                        it.pesoTotal.toString(),
                        it.pesoPromedio.toString()
                    )
                }
            )

            writeCsv(
                File(workDir, "Paquetes_Especiales.csv"),
                headers = listOf("Especial", "Cantidad", "Porcentaje"),
                rows = paquetesEspeciales.map {
                    listOf(
                        if (it.esEspecial == 1) "SI" else "NO",
                        it.cantidad.toString(),
                        it.porcentaje.toString()
                    )
                }
            )

            writeCsv(
                File(workDir, "Raw_Paquetes.csv"),
                headers = listOf("Tracking", "Cedula", "Cliente", "FechaRegistro", "Tienda", "Peso", "ValorBruto", "Moneda", "Especial"),
                rows = paquetes.map {
                    listOf(
                        it.numeroTracking,
                        it.cedulaCliente,
                        it.nombrecliente,
                        it.fechaRegistro.toString(),
                        it.tiendaOrigen.name,
                        it.pesoPaquete.toString(),
                        it.valorBruto.toString(),
                        it.monedasPaquete.name,
                        if (it.condicionEspecial) "SI" else "NO"
                    )
                }
            )
        }

        // ✅ Condicional: Facturación
        if (config.incluirFacturacion) {
            writeCsv(
                File(workDir, "Facturacion_Mensual.csv"),
                headers = listOf("AnioMes", "Total", "CantidadFacturas", "TicketPromedio"),
                rows = facturacionPorMes.map {
                    listOf(
                        it.anioMes,
                        it.totalMonto.toString(),
                        it.cantidadFacturas.toString(),
                        it.ticketPromedio.toString()
                    )
                }
            )

            writeCsv(
                File(workDir, "Facturacion_Clientes.csv"),
                headers = listOf("Cedula", "Total", "CantidadFacturas", "TicketPromedio"),
                rows = facturacionPorCliente.map {
                    listOf(
                        it.cedulaCliente,
                        it.totalMonto.toString(),
                        it.cantidadFacturas.toString(),
                        it.ticketPromedio.toString()
                    )
                }
            )

            writeCsv(
                File(workDir, "Facturacion_Tracking.csv"),
                headers = listOf("Tracking", "Cedula", "Fecha", "Monto"),
                rows = facturacionPorTracking.map {
                    listOf(
                        it.numeroTracking,
                        it.cedulaCliente,
                        it.fechaFacturacion,
                        it.montoTotal.toString()
                    )
                }
            )

            writeCsv(
                File(workDir, "Raw_Facturas.csv"),
                headers = listOf("Id", "Tracking", "Cedula", "Fecha", "MontoTotal", "Direccion"),
                rows = facturas.map {
                    listOf(
                        it.idFacturacion.toString(),
                        it.numeroTracking,
                        it.cedulaCliente,
                        it.fechaFacturacion,
                        it.montoTotal.toString(),
                        it.direccionEntrega
                    )
                }
            )
        }

        // Zip final
        val zipFile = File(context.cacheDir, "ReporteMaestro_Aeropost_CSV.zip")
        zipFolder(workDir, zipFile)

        // Limpieza
        workDir.deleteRecursively()

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            zipFile
        )
    }

    private fun writeCsv(file: File, headers: List<String>, rows: List<List<String>>) {
        BufferedWriter(OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8)).use { w ->
            w.write(headers.joinToString(",") { csvEscape(it) })
            w.newLine()
            for (row in rows) {
                w.write(row.joinToString(",") { csvEscape(it) })
                w.newLine()
            }
        }
    }

    private fun csvEscape(value: String): String {
        val needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")
        val v = value.replace("\"", "\"\"")
        return if (needsQuotes) "\"$v\"" else v
    }

    private fun zipFolder(folder: File, outZip: File) {
        ZipOutputStream(FileOutputStream(outZip)).use { zos ->
            folder.listFiles()?.forEach { file ->
                if (file.isFile) {
                    FileInputStream(file).use { fis ->
                        zos.putNextEntry(ZipEntry(file.name))
                        fis.copyTo(zos)
                        zos.closeEntry()
                    }
                }
            }
        }
    }
}
