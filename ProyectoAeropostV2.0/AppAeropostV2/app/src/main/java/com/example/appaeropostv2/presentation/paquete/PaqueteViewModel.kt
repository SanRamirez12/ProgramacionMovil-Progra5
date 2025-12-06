package com.example.appaeropostv2.presentation.paquete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryPaquete
import com.example.appaeropostv2.domain.model.Paquete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate

class PaqueteViewModelFactory(
    private val repositoryPaquete: RepositoryPaquete
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaqueteViewModel::class.java)) {
            return PaqueteViewModel(repositoryPaquete) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

data class PaqueteUiState(
    val isLoading: Boolean = false,
    val paquetes: List<Paquete> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val fechaDesde: LocalDate? = null,
    val fechaHasta: LocalDate? = null
)

class PaqueteViewModel(
    private val repository: RepositoryPaquete
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaqueteUiState(isLoading = true))
    val uiState: StateFlow<PaqueteUiState> = _uiState.asStateFlow()

    private var todosLosPaquetes: List<Paquete> = emptyList()

    init {
        observarPaquetes()
    }

    private fun observarPaquetes() {
        viewModelScope.launch {
            repository.observarPaquetes()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = e.message,
                        isLoading = false
                    )
                }
                .collect { lista ->
                    todosLosPaquetes = lista
                    aplicarFiltros()
                }
        }
    }

    private fun aplicarFiltros() {
        val estadoActual = _uiState.value
        val q = estadoActual.searchQuery.trim().lowercase()
        val desde = estadoActual.fechaDesde
        val hasta = estadoActual.fechaHasta

        var lista = todosLosPaquetes

        // Filtro por rango de fechas
        if (desde != null || hasta != null) {
            lista = lista.filter { paquete ->
                val f = paquete.fechaRegistro
                val cumpleDesde = desde?.let { !f.isBefore(it) } ?: true
                val cumpleHasta = hasta?.let { !f.isAfter(it) } ?: true
                cumpleDesde && cumpleHasta
            }
        }

        // Filtro por texto: nombrecliente, cédula, tracking
        if (q.isNotBlank()) {
            lista = lista.filter { paquete ->
                val nombre = paquete.nombrecliente.lowercase()
                val cedula = paquete.cedulaCliente.lowercase()
                val tracking = paquete.numeroTracking.lowercase()
                nombre.contains(q) || cedula.contains(q) || tracking.contains(q)
            }
        }

        _uiState.value = estadoActual.copy(
            paquetes = lista,
            isLoading = false,
            errorMessage = null
        )
    }

    // --- Búsqueda de texto ---
    fun actualizarBusqueda(texto: String) {
        _uiState.value = _uiState.value.copy(searchQuery = texto)
        aplicarFiltros()
    }

    // --- Filtro de fechas ---
    fun actualizarFechaDesde(fecha: LocalDate?) {
        _uiState.value = _uiState.value.copy(fechaDesde = fecha)
        aplicarFiltros()
    }

    fun actualizarFechaHasta(fecha: LocalDate?) {
        _uiState.value = _uiState.value.copy(fechaHasta = fecha)
        aplicarFiltros()
    }

    fun limpiarFechas() {
        _uiState.value = _uiState.value.copy(fechaDesde = null, fechaHasta = null)
        aplicarFiltros()
    }

    // --- CRUD ---
    fun insertar(paquete: Paquete) {
        viewModelScope.launch {
            try {
                repository.insertar(paquete)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun actualizar(paquete: Paquete) {
        viewModelScope.launch {
            try {
                repository.actualizar(paquete)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun eliminar(paquete: Paquete) {
        viewModelScope.launch {
            try {
                repository.eliminar(paquete)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    suspend fun obtenerPorId(idPaquete: String): Paquete? =
        repository.obtenerPorId(idPaquete)

    suspend fun obtenerPorTracking(tracking: String): Paquete? =
        repository.obtenerPorTracking(tracking)
}
