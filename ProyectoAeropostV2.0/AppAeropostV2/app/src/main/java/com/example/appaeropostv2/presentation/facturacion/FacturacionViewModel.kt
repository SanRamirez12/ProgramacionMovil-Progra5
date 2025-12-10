package com.example.appaeropostv2.presentation.facturacion

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryCliente
import com.example.appaeropostv2.data.repository.RepositoryFacturacion
import com.example.appaeropostv2.data.repository.RepositoryPaquete
import com.example.appaeropostv2.domain.enums.Monedas
import com.example.appaeropostv2.domain.logic.FacturaLogic
import com.example.appaeropostv2.domain.model.*
import com.example.appaeropostv2.interfaces.InterfaceFacturaPdfGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDate

// =======================================================
// FACTORY
// =======================================================
class FacturacionViewModelFactory(
    private val repoFacturacion: RepositoryFacturacion,
    private val repoCliente: RepositoryCliente,
    private val repoPaquete: RepositoryPaquete,
    private val pdfGenerator: InterfaceFacturaPdfGenerator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(model: Class<T>): T {
        if (model.isAssignableFrom(FacturacionViewModel::class.java)) {
            return FacturacionViewModel(
                repoFacturacion,
                repoCliente,
                repoPaquete,
                pdfGenerator
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// =======================================================
// UI STATE
// =======================================================
data class FacturacionUiState(
    val isLoading: Boolean = false,
    val facturas: List<Facturacion> = emptyList(),

    val searchQuery: String = "",
    val fechaDesde: LocalDate? = null,
    val fechaHasta: LocalDate? = null,

    val clienteCargado: Cliente? = null,
    val paqueteCargado: Paquete? = null,
    val paquetesDisponibles: List<Paquete> = emptyList(),

    val fechaFacturacion: String = "",
    val cedulaInput: String = "",
    val trackingInput: String = "",

    val montoTotalCalculado: Double? = null,

    // mapa para saber la moneda de cada factura por tracking
    val monedaPorTracking: Map<String, Monedas> = emptyMap(),

    val errorMessage: String? = null,
    val pdfUri: Uri? = null
)

// =======================================================
// VIEWMODEL
// =======================================================
class FacturacionViewModel(
    private val repoFacturacion: RepositoryFacturacion,
    private val repoCliente: RepositoryCliente,
    private val repoPaquete: RepositoryPaquete,
    private val pdfGenerator: InterfaceFacturaPdfGenerator
) : ViewModel() {

    private val _ui = MutableStateFlow(FacturacionUiState(isLoading = true))
    val ui: StateFlow<FacturacionUiState> = _ui.asStateFlow()

    // lista completa (sin filtros), igual que todosLosPaquetes en PaqueteViewModel
    private var todasLasFacturas: List<Facturacion> = emptyList()

    init {
        observarFacturas()
    }

    // ===================================================
    // OBSERVAR FACTURAS
    // ===================================================
    private fun observarFacturas() {
        viewModelScope.launch {
            repoFacturacion.observarFacturaciones()
                .catch { e ->
                    _ui.value = _ui.value.copy(
                        errorMessage = e.message,
                        isLoading = false
                    )
                }
                .collect { lista ->
                    todasLasFacturas = lista
                    val mapaMonedas = construirMapaMonedas(lista)

                    // actualizamos solo el mapa y luego aplicamos filtros
                    _ui.value = _ui.value.copy(
                        monedaPorTracking = mapaMonedas
                    )

                    aplicarFiltros()
                }
        }
    }

    // Construye mapa tracking → moneda usando el paquete asociado
    private suspend fun construirMapaMonedas(lista: List<Facturacion>): Map<String, Monedas> {
        val mapa = mutableMapOf<String, Monedas>()
        for (factura in lista) {
            val paquete = repoPaquete.obtenerPorTracking(factura.numeroTracking)
            if (paquete != null) {
                mapa[factura.numeroTracking] = paquete.monedasPaquete
            }
        }
        return mapa
    }

    // ===================================================
    // APLICAR FILTROS (búsqueda + fechas)
    // ===================================================
    private fun aplicarFiltros() {
        val estadoActual = _ui.value
        val q = estadoActual.searchQuery.trim().lowercase()
        val desde = estadoActual.fechaDesde
        val hasta = estadoActual.fechaHasta

        var lista = todasLasFacturas

        // Filtro por rango de fechas
        if (desde != null || hasta != null) {
            lista = lista.filter { factura ->
                val f = LocalDate.parse(factura.fechaFacturacion)
                val cumpleDesde = desde?.let { !f.isBefore(it) } ?: true
                val cumpleHasta = hasta?.let { !f.isAfter(it) } ?: true
                cumpleDesde && cumpleHasta
            }
        }

        // Filtro por texto: cédula o tracking
        if (q.isNotBlank()) {
            lista = lista.filter { factura ->
                val cedula = factura.cedulaCliente.lowercase()
                val tracking = factura.numeroTracking.lowercase()
                cedula.contains(q) || tracking.contains(q)
            }
        }

        _ui.value = estadoActual.copy(
            facturas = lista,
            isLoading = false,
            errorMessage = null
        )
    }

    // ===================================================
    // INPUTS DEL FORMULARIO
    // ===================================================
    fun actualizarFechaFacturacion(fecha: String) {
        _ui.value = _ui.value.copy(fechaFacturacion = fecha)
    }

    fun actualizarCedula(cedula: String) {
        _ui.value = _ui.value.copy(cedulaInput = cedula)
    }

    fun actualizarTracking(tracking: String) {
        _ui.value = _ui.value.copy(trackingInput = tracking)
    }

    // ===================================================
    // CARGAR CLIENTE + PAQUETES DISPONIBLES
    // ===================================================
    fun cargarCliente() {
        viewModelScope.launch {
            val cedula = _ui.value.cedulaInput.trim()
            val cliente = repoCliente.obtenerPorCedula(cedula)

            if (cliente == null) {
                _ui.value = _ui.value.copy(errorMessage = "Cliente no encontrado.")
                return@launch
            }

            if (cliente.estadoCliente.name != "HABILITADO") {
                _ui.value = _ui.value.copy(errorMessage = "Este cliente está deshabilitado.")
                return@launch
            }

            val paquetesCliente = repoPaquete.listarPorCedula(cedula)

            val paquetesSinFacturar = paquetesCliente.filter { paquete ->
                repoFacturacion.obtenerPorTracking(paquete.numeroTracking) == null
            }

            _ui.value = _ui.value.copy(
                clienteCargado = cliente,
                paquetesDisponibles = paquetesSinFacturar,
                paqueteCargado = null,
                montoTotalCalculado = null,
                trackingInput = ""
            )

            if (paquetesSinFacturar.isEmpty()) {
                _ui.value = _ui.value.copy(
                    errorMessage = "Este cliente no tiene paquetes pendientes de facturar."
                )
            }
        }
    }

    // ===================================================
    // CARGAR PAQUETE (modo antiguo por tracking)
    // ===================================================
    fun cargarPaquete() {
        viewModelScope.launch {
            val tracking = _ui.value.trackingInput.trim()
            val paquete = repoPaquete.obtenerPorTracking(tracking)

            if (paquete == null) {
                _ui.value = _ui.value.copy(errorMessage = "Paquete no encontrado.")
                return@launch
            }

            val cliente = _ui.value.clienteCargado
            if (cliente == null || paquete.cedulaCliente != cliente.cedulaCliente) {
                _ui.value = _ui.value.copy(errorMessage = "El paquete no pertenece a este cliente.")
                return@launch
            }

            val yaFacturado = repoFacturacion.obtenerPorTracking(tracking)
            if (yaFacturado != null) {
                _ui.value = _ui.value.copy(errorMessage = "Este paquete ya fue facturado.")
                return@launch
            }

            _ui.value = _ui.value.copy(paqueteCargado = paquete)

            calcularMonto()
        }
    }

    // ===================================================
    // SELECCIONAR PAQUETE DESDE DROPDOWN
    // ===================================================
    fun seleccionarPaquete(paquete: Paquete) {
        viewModelScope.launch {
            val yaFacturado = repoFacturacion.obtenerPorTracking(paquete.numeroTracking)
            if (yaFacturado != null) {
                _ui.value = _ui.value.copy(errorMessage = "Este paquete ya fue facturado.")
                return@launch
            }

            _ui.value = _ui.value.copy(
                paqueteCargado = paquete,
                trackingInput = paquete.numeroTracking
            )

            calcularMonto()
        }
    }

    // ===================================================
    // CÁLCULO AUTOMÁTICO DE MONTO
    // ===================================================
    private fun calcularMonto() {
        val paq = _ui.value.paqueteCargado ?: return
        val monto = FacturaLogic.calcularMontoTotal(
            pesoPaquete = paq.pesoPaquete,
            valorBruto = paq.valorBruto.toDouble(),
            productoEspecial = paq.condicionEspecial
        )

        _ui.value = _ui.value.copy(montoTotalCalculado = monto)
    }

    // ===================================================
    // GENERAR FACTURA
    // ===================================================
    fun generarFactura() {
        viewModelScope.launch {
            val cliente = _ui.value.clienteCargado
            val paquete = _ui.value.paqueteCargado
            val fecha = _ui.value.fechaFacturacion
            val monto = _ui.value.montoTotalCalculado

            if (cliente == null || paquete == null || fecha.isBlank() || monto == null) {
                _ui.value = _ui.value.copy(errorMessage = "Complete todos los datos.")
                return@launch
            }

            val factura = Facturacion(
                idFacturacion = 0,
                numeroTracking = paquete.numeroTracking,
                cedulaCliente = cliente.cedulaCliente,
                pesoPaquete = paquete.pesoPaquete,
                valorBrutoPaquete = paquete.valorBruto.toDouble(),
                productoEspecial = paquete.condicionEspecial,
                fechaFacturacion = fecha,
                montoTotal = monto,
                direccionEntrega = cliente.direccionEntrega
            )

            val idInsertado = repoFacturacion.insertar(factura)

            val detalle = FacturaConDetalle(
                factura.copy(idFacturacion = idInsertado),
                cliente,
                paquete
            )

            val pdfUri = pdfGenerator.generarFacturaPDF(detalle)

            _ui.value = _ui.value.copy(
                pdfUri = pdfUri,
                errorMessage = null
            )
        }
    }

    // ===================================================
    // FILTROS LISTA FACTURAS
    // ===================================================
    fun actualizarBusqueda(q: String) {
        _ui.value = _ui.value.copy(searchQuery = q)
        aplicarFiltros()
    }

    fun actualizarFechaDesde(f: LocalDate?) {
        _ui.value = _ui.value.copy(fechaDesde = f)
        aplicarFiltros()
    }

    fun actualizarFechaHasta(f: LocalDate?) {
        _ui.value = _ui.value.copy(fechaHasta = f)
        aplicarFiltros()
    }

    fun limpiarFechas() {
        _ui.value = _ui.value.copy(fechaDesde = null, fechaHasta = null)
        aplicarFiltros()
    }

    // ===================================================
    // LIMPIAR EVENTOS
    // ===================================================
    fun limpiarPdfUri() {
        _ui.value = _ui.value.copy(pdfUri = null)
    }

    fun limpiarError() {
        _ui.value = _ui.value.copy(errorMessage = null)
    }

    // ===================================================
    // ELIMINAR FACTURA
    // ===================================================
    fun eliminarFactura(factura: Facturacion) {
        viewModelScope.launch {
            try {
                repoFacturacion.eliminar(factura)
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(errorMessage = e.message)
            }
        }
    }

    // ===================================================
    // GENERAR PDF PARA FACTURA EXISTENTE
    // ===================================================
    fun generarPdfParaFacturaExistente(factura: Facturacion) {
        viewModelScope.launch {
            try {
                val cliente = repoCliente.obtenerPorCedula(factura.cedulaCliente)
                    ?: run {
                        _ui.value = _ui.value.copy(errorMessage = "Cliente de la factura no existe.")
                        return@launch
                    }

                val paquete = repoPaquete.obtenerPorTracking(factura.numeroTracking)
                    ?: run {
                        _ui.value = _ui.value.copy(errorMessage = "Paquete de la factura no existe.")
                        return@launch
                    }

                val detalle = FacturaConDetalle(
                    factura = factura,
                    cliente = cliente,
                    paquete = paquete
                )

                val pdfUri = pdfGenerator.generarFacturaPDF(detalle)

                _ui.value = _ui.value.copy(
                    pdfUri = pdfUri,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _ui.value = _ui.value.copy(errorMessage = e.message)
            }
        }
    }
}
