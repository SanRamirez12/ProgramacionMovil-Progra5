package com.example.appaeropost.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.clientes.ClientesRepository
import com.example.appaeropost.domain.clientes.Cliente
import com.example.appaeropost.domain.clientes.EstadoCliente
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClienteUi(
    val id: Int,
    val nombre: String,
    val cedula: String,
    val habilitado: Boolean
)


private fun Cliente.toUi(): ClienteUi = ClienteUi(
    id = id,                       // ← Int directo
    nombre = nombre,
    cedula = identificacion,
    habilitado = (estado == EstadoCliente.HABILITADO)
)


class ClientesViewModel(
    private val repo: ClientesRepository
) : ViewModel() {

    data class State(
        val title: String = "Clientes",
        val query: String = "",
        val rows: List<ClienteUi> = emptyList(),
        val isLoading: Boolean = false,
        val canLoadMore: Boolean = false,
        val error: String? = null,
        val pageSize: Int = 25,
        val currentOffset: Int = 0
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private var debounceJob: Job? = null

    fun onQueryChange(newValue: String) {
        _state.update { it.copy(query = newValue) }
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(300)
            search(reset = true)
        }
    }

    fun search(reset: Boolean) {
        viewModelScope.launch {
            val st0 = _state.value
            val offset = if (reset) 0 else st0.currentOffset + st0.pageSize
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val clientes: List<Cliente> = if (st0.query.isBlank()) {
                    repo.getClientes(limit = st0.pageSize, offset = offset)
                } else {
                    repo.searchClientes(
                        query = st0.query.trim(),
                        limit = st0.pageSize,
                        offset = offset
                    )
                }
                val result = clientes.map { it.toUi() }
                val merged = if (reset) result else st0.rows + result
                _state.update {
                    it.copy(
                        rows = merged,
                        isLoading = false,
                        canLoadMore = result.size == it.pageSize,
                        currentOffset = offset
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error") }
            }
        }
    }
    init {
        // Carga inicial para que la tabla no se vea vacía
        search(reset = true)
    }

    fun loadMore() = search(reset = false)

    // --------- CRUD para pantallas ---------

    private fun fromForm(state: ClienteFormState, id: Int = 0): Cliente =
        Cliente(
            id = id,
            nombre = state.nombre,
            identificacion = state.identificacion,
            telefono = state.telefono,
            correo = state.correo,
            tipo = state.tipo,
            estado = state.estado,
            direccionEntrega = state.direccionEntrega
        )

    fun crearCliente(state: ClienteFormState, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repo.create(fromForm(state))
                onDone()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Error al crear cliente") }
            }
        }
    }

    suspend fun cargarClienteById(id: Int): Cliente? =
        try { repo.getById(id) } catch (_: Exception) { null }

    fun actualizarCliente(id: Int, state: ClienteFormState, onDone: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val ok = repo.update(fromForm(state, id))
                onDone(ok)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Error al actualizar cliente") }
                onDone(false)
            }
        }
    }


}


