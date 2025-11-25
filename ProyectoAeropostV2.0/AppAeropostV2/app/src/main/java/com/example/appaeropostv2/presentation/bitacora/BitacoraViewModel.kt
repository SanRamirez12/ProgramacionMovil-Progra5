package com.example.appaeropostv2.presentation.bitacora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryBitacora
import com.example.appaeropostv2.domain.model.Bitacora
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class BitacoraUiState(
    val isLoading: Boolean = false,
    val eventos: List<Bitacora> = emptyList(),
    val errorMessage: String? = null
)

class BitacoraViewModel(
    private val repositoryBitacora: RepositoryBitacora
) : ViewModel() {

    private val _uiState = MutableStateFlow(BitacoraUiState(isLoading = true))
    val uiState: StateFlow<BitacoraUiState> = _uiState.asStateFlow()

    init {
        observarBitacora()
    }

    private fun observarBitacora() {
        viewModelScope.launch {
            repositoryBitacora.observarEventos()
                .onStart {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { lista ->
                    _uiState.value = BitacoraUiState(
                        isLoading = false,
                        eventos = lista,
                        errorMessage = null
                    )
                }
        }
    }
}

class BitacoraViewModelFactory(
    private val repositoryBitacora: RepositoryBitacora
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BitacoraViewModel::class.java)) {
            return BitacoraViewModel(repositoryBitacora) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
