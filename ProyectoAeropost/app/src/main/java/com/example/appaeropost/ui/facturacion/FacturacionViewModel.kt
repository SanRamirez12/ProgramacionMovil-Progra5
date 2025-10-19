package com.example.appaeropost.ui.facturacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.facturacion.FacturacionRepository
import com.example.appaeropost.data.facturacion.FakeFacturacionRepository
import com.example.appaeropost.domain.Factura
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClienteMini(
    val cedula: String,
    val nombre: String,
    val correo: String,
    val telefono: String,
    val direccion: String
)

data class PaqueteMini(
    val tracking: String,
    val cedulaCliente: String,
    val pesoKg: Double,
    val valorEstimado: Double,
    val esEspecial: Boolean,
    val fechaEntrega: String
)

data class FacturacionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<Factura> = emptyList()
)

class FacturacionViewModel(
    private val repo: FacturacionRepository = FakeFacturacionRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(FacturacionState())
    val ui: StateFlow<FacturacionState> = _ui

    // Fakes para el demo de “jalar datos”
    private val clientesFake = listOf(
        ClienteMini("118250091", "Jose Alberto", "jose@gmail.com", "63904892", "Calle 1"),
        ClienteMini("207890456", "Santiago", "santi@example.com", "88887777", "San José")
    )
    private val paquetesFake = listOf(
        PaqueteMini("SH0123CR-AIR-0001", "118250091", 2.6, 78000.0, false, "2025-08-21"),
        PaqueteMini("GHI2023TRK0002", "207890456", 5.1, 120000.0, true, "2025-09-01")
    )

    init { recargar() }

    fun recargar() {
        viewModelScope.launch {
            _ui.update { it.copy(isLoading = true, error = null) }
            runCatching { repo.listarFacturas() }
                .onSuccess { list -> _ui.update { it.copy(isLoading = false, items = list) } }
                .onFailure { e -> _ui.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    fun buscarClientePorCedula(cedula: String): ClienteMini? =
        clientesFake.firstOrNull { it.cedula == cedula }

    fun paquetesPorCliente(cedula: String): List<PaqueteMini> =
        paquetesFake.filter { it.cedulaCliente == cedula }

    fun paquetePorTracking(tracking: String): PaqueteMini? =
        paquetesFake.firstOrNull { it.tracking == tracking }

    fun crear(f: Factura, onOk: (Factura) -> Unit, onErr: (String) -> Unit) {
        viewModelScope.launch {
            repo.crearFactura(f)
                .onSuccess { onOk(it); recargar() }
                .onFailure { onErr(it.message ?: "Error desconocido") }
        }
    }

    fun actualizar(f: Factura, onOk: (Factura) -> Unit, onErr: (String) -> Unit) {
        viewModelScope.launch {
            repo.actualizarFactura(f)
                .onSuccess { onOk(it); recargar() }
                .onFailure { onErr(it.message ?: "Error desconocido") }
        }
    }
}
