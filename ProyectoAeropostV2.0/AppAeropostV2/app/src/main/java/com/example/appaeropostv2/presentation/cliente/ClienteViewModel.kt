package com.example.appaeropostv2.presentation.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryCliente
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Cliente
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ClienteViewModelFactory(
    private val repositoryCliente: RepositoryCliente
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(repositoryCliente) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

data class ClienteUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = ""
)

class ClienteViewModel(
    private val repository: RepositoryCliente
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClienteUiState(isLoading = true))
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    init {
        observarClientes()
    }

    // Observa en tiempo real los clientes almacenados en Room
    private fun observarClientes() {
        viewModelScope.launch {
            repository.observarClientes()
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
                    val q = _uiState.value.searchQuery.lowercase()

                    // Filtrado dinámico por nombre o cédula (como pediste)
                    val filtrados =
                        if (q.isBlank()) lista
                        else lista.filter { cliente ->
                            val nombre = cliente.nombreCliente.lowercase()
                            val cedula = cliente.cedulaCliente.lowercase()

                            nombre.contains(q) || cedula.contains(q)
                        }

                    _uiState.value = _uiState.value.copy(
                        clientes = filtrados,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    // Actualiza búsqueda
    fun actualizarBusqueda(texto: String) {
        _uiState.value = _uiState.value.copy(searchQuery = texto)
        observarClientes() // recalcula el filtrado dinámico
    }

    // CRUD ----------------------

    fun insertar(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repository.insertar(cliente)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun actualizar(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repository.actualizar(cliente)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun eliminar(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repository.eliminar(cliente)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    // Cambiar estado (habilitar ↔ deshabilitar)
    fun cambiarEstadoCliente(id: Int) {
        viewModelScope.launch {
            try {
                val cliente = repository.obtenerPorId(id) ?: return@launch

                val nuevoEstado =
                    if (cliente.estadoCliente == Estados.HABILITADO)
                        Estados.DESHABILITADO
                    else
                        Estados.HABILITADO

                val actualizado = cliente.copy(estadoCliente = nuevoEstado)
                repository.actualizar(actualizado)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    suspend fun obtenerPorId(id: Int): Cliente? = repository.obtenerPorId(id)
}

