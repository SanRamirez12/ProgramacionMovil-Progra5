package com.example.appaeropost.ui.paquetes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.paquetes.FakePaquetesRepository
import com.example.appaeropost.domain.paquetes.Paquete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaquetesState(
    val isLoading: Boolean = false,
    val cedulaQuery: String = "",
    val items: List<Paquete> = emptyList(),
    val hasSearched: Boolean = false,
    val error: String? = null
)

class PaquetesViewModel(
    private val repo: FakePaquetesRepository = FakePaquetesRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(PaquetesState())
    val ui: StateFlow<PaquetesState> = _ui

    // Igual que Clientes: sin resultados hasta que el usuario escriba
    fun onCedulaChange(q: String) {
        _ui.update { it.copy(cedulaQuery = q) }
        search()
    }

    private fun search() {
        viewModelScope.launch {
            val q = _ui.value.cedulaQuery.trim()
            if (q.isEmpty()) {
                _ui.update { it.copy(items = emptyList(), hasSearched = false, isLoading = false, error = null) }
                return@launch
            }
            _ui.update { it.copy(isLoading = true, hasSearched = true, error = null) }
            runCatching { repo.searchByCedula(q) }
                .onSuccess { list -> _ui.update { it.copy(items = list, isLoading = false) } }
                .onFailure { e -> _ui.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}



