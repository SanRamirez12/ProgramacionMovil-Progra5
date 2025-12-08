package com.example.appaeropostv2.presentation.facturacion

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryCliente
import com.example.appaeropostv2.data.repository.RepositoryFacturacion
import com.example.appaeropostv2.data.repository.RepositoryPaquete
import com.example.appaeropostv2.domain.logic.FacturaLogic
import com.example.appaeropostv2.domain.model.*
import com.example.appaeropostv2.domain.pdf.FacturaPdfGenerator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

// =======================================================
// FACTORY
// =======================================================
class FacturacionViewModelFactory(
    private val repoFacturacion: RepositoryFacturacion,
    private val repoCliente: RepositoryCliente,
    private val repoPaquete: RepositoryPaquete,
    private val pdfGenerator: FacturaPdfGenerator
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

    val fechaFacturacion: String = "",
    val cedulaInput: String = "",
    val trackingInput: String = "",

    val montoTotalCalculado: Double? = null,

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
    private val pdfGenerator: FacturaPdfGenerator
) : ViewModel() {

    private val _ui = MutableStateFlow(FacturacionUiState())
    val ui: StateFlow<FacturacionUiState> = _ui.asStateFlow()

    init {
        observarFacturas()
    }

    // ===================================================
    // OBSERVAR FACTURAS
    // ===================================================
    private fun observarFacturas() {
        viewModelScope.launch {
            repoFacturacion.observarFacturaciones()
                .catch { e -> _ui.value = _ui.value.copy(errorMessage = e.message) }
                .collect { lista ->
                    _ui.value = _ui.value.copy(
                        facturas = filtrarFacturas(lista),
                        isLoading = false
                    )
                }
        }
    }

    private fun filtrarFacturas(lista: List<Facturacion>): List<Facturacion> {
        val q = _ui.value.searchQuery.lowercase()
        val desde = _ui.value.fechaDesde
        val hasta = _ui.value.fechaHasta

        return lista.filter { f ->

            // filtro por texto
            val coincideTexto = q.isBlank() ||
                    f.numeroTracking.lowercase().contains(q) ||
                    f.cedulaCliente.lowercase().contains(q)

            // filtro por fecha
            val coincideFecha = when {
                desde == null && hasta == null -> true
                desde != null && hasta == null ->
                    LocalDate.parse(f.fechaFacturacion) >= desde
                desde == null && hasta != null ->
                    LocalDate.parse(f.fechaFacturacion) <= hasta
                else ->
                    LocalDate.parse(f.fechaFacturacion) in desde!!..hasta!!
            }

            coincideTexto && coincideFecha
        }
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
    // CARGAR CLIENTE
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

            _ui.value = _ui.value.copy(clienteCargado = cliente)
        }
    }

    // ===================================================
    // CARGAR PAQUETE
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

            // validar que no esté facturado
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

            // construir FacturaConDetalle
            val detalle = FacturaConDetalle(
                factura.copy(idFacturacion = idInsertado),
                cliente,
                paquete
            )

            // Generar el PDF
            val pdfUri = pdfGenerator.generarFacturaPDF(detalle)

            _ui.value = _ui.value.copy(
                pdfUri = pdfUri,
                errorMessage = null
            )
        }
    }

    // ===================================================
    // FILTROS
    // ===================================================
    fun actualizarBusqueda(q: String) {
        _ui.value = _ui.value.copy(searchQuery = q)
        observarFacturas()
    }

    fun actualizarFechaDesde(f: LocalDate?) {
        _ui.value = _ui.value.copy(fechaDesde = f)
        observarFacturas()
    }

    fun actualizarFechaHasta(f: LocalDate?) {
        _ui.value = _ui.value.copy(fechaHasta = f)
        observarFacturas()
    }

    fun limpiarFechas() {
        _ui.value = _ui.value.copy(fechaDesde = null, fechaHasta = null)
        observarFacturas()
    }

    // ===================================================
    // LIMPIAR PDF EVENT
    // ===================================================
    fun limpiarPdfUri() {
        _ui.value = _ui.value.copy(pdfUri = null)
    }

    fun limpiarError() {
        _ui.value = _ui.value.copy(errorMessage = null)
    }
}
