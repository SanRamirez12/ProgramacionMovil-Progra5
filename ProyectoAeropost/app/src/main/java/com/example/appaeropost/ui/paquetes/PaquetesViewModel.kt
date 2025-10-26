package com.example.appaeropost.ui.paquetes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.config.ServiceLocator
import com.example.appaeropost.data.paquetes.PaquetesRepository
import com.example.appaeropost.domain.paquetes.Paquete
import com.example.appaeropost.domain.paquetes.PaqueteCancelado
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

class PaquetesViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: PaquetesRepository by lazy {
        ServiceLocator.paquetesRepository(app.applicationContext)
    }

    private val _ui = MutableStateFlow(PaquetesState())
    val ui: StateFlow<PaquetesState> = _ui

    fun setQuery(q: String) {
        _ui.update { it.copy(cedulaQuery = q) }
    }

    // Alias para tu UI actual (PaquetesScreen usa vm::onCedulaChange)
    fun onCedulaChange(value: String) = setQuery(value)

    fun search(reset: Boolean = true) {
        viewModelScope.launch {
            val q = ui.value.cedulaQuery.trim()
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

    fun upsert(paquete: Paquete, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching { repo.upsert(paquete) }
                .onSuccess { onDone() }
                .onFailure { e -> _ui.update { it.copy(error = e.message) } }
        }
    }

    fun cancelarPaquete(paqueteId: String, motivo: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching { repo.cancelar(paqueteId, motivo) }
                .onSuccess { onDone() }
                .onFailure { e -> _ui.update { it.copy(error = e.message) } }
        }
    }

    fun restaurarPaquete(paqueteId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            runCatching { repo.restaurar(paqueteId) }
                .onSuccess { onDone() }
                .onFailure { e -> _ui.update { it.copy(error = e.message) } }
        }
    }

    suspend fun getCancelados(): List<PaqueteCancelado> =
        runCatching { repo.getCancelados() }.getOrElse { emptyList() }
}


