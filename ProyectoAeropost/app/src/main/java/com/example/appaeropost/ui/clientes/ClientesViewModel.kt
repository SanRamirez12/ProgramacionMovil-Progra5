package com.example.appaeropost.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropost.data.clientes.ClientesRepository
import com.example.appaeropost.domain.clientes.Cliente
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClientesUiState(
    val isLoading: Boolean = true,
    val query: String = "",
    val items: List<Cliente> = emptyList(),
    val error: String? = null
)

class ClientesViewModel(
    private val repo: ClientesRepository = ClientesRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(ClientesUiState())
    val state: StateFlow<ClientesUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init { loadAll() }

    private fun loadAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { repo.getClientes() }
                .onSuccess { _state.value = _state.value.copy(isLoading = false, items = it) }
                .onFailure { _state.value = _state.value.copy(isLoading = false, error = it.message) }
        }
    }

    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(250)
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching { repo.searchClientes(newQuery) }
                .onSuccess { _state.value = _state.value.copy(isLoading = false, items = it) }
                .onFailure { _state.value = _state.value.copy(isLoading = false, error = it.message) }
        }
    }
}
