package com.example.appaeropost.ui.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.repository.FakeUsuariosRepository
import com.example.appaeropost.data.repository.UsuariosRepository
import com.example.appaeropost.domain.Usuario
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.min

data class UsuarioUi(
    val id: String,        // usamos username como id estable
    val nombre: String,
    val cedula: String,
    val rol: String
)

class UsuarioViewModel(
    private val repo: UsuariosRepository = FakeUsuariosRepository()
) : ViewModel() {

    data class State(
        val title: String = "Usuarios",
        val query: String = "",
        val rows: List<UsuarioUi> = emptyList(),
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
            val q = st0.query.trim()
            val offset = if (reset) 0 else st0.currentOffset + st0.pageSize

            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // FakeRepo no pagina; aquí realizamos un “slice” local para emular paginado
                val all = if (q.isEmpty()) {
                    // Para mantener la UX de Clientes: sin query → lista vacía hasta que busquen
                    emptyList()
                } else {
                    repo.buscar(q).map { it.toUi() }
                }

                val end = min(offset + st0.pageSize, all.size)
                val page = if (offset >= all.size) emptyList() else all.subList(offset, end)
                val merged = if (reset) page else st0.rows + page

                _state.update {
                    it.copy(
                        rows = merged,
                        isLoading = false,
                        canLoadMore = page.size == it.pageSize && end < all.size,
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

/* -------- Extensiones -------- */

private fun Usuario.toUi() = UsuarioUi(
    id = username,
    nombre = nombre,
    cedula = cedula,
    rol = rol.name.lowercase(Locale.getDefault()).replaceFirstChar { it.titlecase(Locale.getDefault()) }
)
