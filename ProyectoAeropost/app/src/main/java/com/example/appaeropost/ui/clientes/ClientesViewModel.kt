package com.example.appaeropost.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.min

data class ClienteUi(
    val id: String,
    val nombre: String,
    val cedula: String,
    val habilitado: Boolean
)

interface ClientesRepository {
    suspend fun buscar(query: String, limit: Int, offset: Int): List<ClienteUi>
}

/** Mock temporal. Luego lo reemplazamos por Room o Firebase */
class InMemoryClientesRepo : ClientesRepository {
    private val data = (1..120).map {
        val enabled = it % 7 != 0
        ClienteUi(
            id = it.toString(),
            nombre = listOf("Ana Morales", "Beto Jiménez", "Carlos Pérez", "Diana Gómez", "Esteban Vega", "María López", "Juan Pérez")[it % 7] + " $it",
            cedula = "${(1..3).random()}-${(1000..9999).random()}-${(1000..9999).random()}",
            habilitado = enabled
        )
    }

    override suspend fun buscar(query: String, limit: Int, offset: Int): List<ClienteUi> {
        val q = query.trim().lowercase(Locale.getDefault())
        val base = if (q.isEmpty()) emptyList() else data.filter {
            it.nombre.lowercase().contains(q) || it.cedula.lowercase().contains(q)
        }
        val end = min(offset + limit, base.size)
        return if (offset >= base.size) emptyList() else base.subList(offset, end)
    }
}

class ClientesViewModel(
    private val repo: ClientesRepository = InMemoryClientesRepo()
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
        // Debounce pequeño para no disparar búsqueda en cada tecla
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
                val result = repo.buscar(st0.query, st0.pageSize, offset)
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

    fun loadMore() = search(reset = false)
}
