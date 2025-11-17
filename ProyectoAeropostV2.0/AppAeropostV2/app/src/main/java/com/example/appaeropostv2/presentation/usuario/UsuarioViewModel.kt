package com.example.appaeropostv2.presentation.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.data.repository.RepositoryUsuario
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class UsuarioViewModelFactory(
    private val repositoryUsuario: RepositoryUsuario
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(repositoryUsuario) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

data class UsuarioUiState(
    val isLoading: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val errorMessage: String? = null,
    val searchQuery: String = ""
)

class UsuarioViewModel(
    private val repository: RepositoryUsuario
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState(isLoading = true))
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    init {
        observarUsuarios()
    }

    // Observa en tiempo real los usuarios almacenados en Room
    private fun observarUsuarios() {
        viewModelScope.launch {
            repository.observarUsuarios()
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

                    // Filtrado dinámico
                    val filtrados =
                        if (q.isBlank()) lista
                        else lista.filter {
                            it.nombreUsuario.lowercase().contains(q) ||
                                    it.cedulaUsuario.lowercase().contains(q) ||
                                    it.username.lowercase().contains(q)
                        }

                    _uiState.value = _uiState.value.copy(
                        usuarios = filtrados,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    // Actualiza búsqueda
    fun actualizarBusqueda(texto: String) {
        _uiState.value = _uiState.value.copy(searchQuery = texto)
        observarUsuarios() // recalcula el filtrado dinámico
    }

    // CRUD ----------------------

    fun insertar(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repository.insertar(usuario)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun actualizar(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repository.actualizar(usuario)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    fun eliminar(usuario: Usuario) {
        viewModelScope.launch {
            try {
                repository.eliminar(usuario)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    // Lógica para "deshabilitar" usuario → cambia su estado
    fun deshabilitar(id: Int) {
        viewModelScope.launch {
            try {
                val usuario = repository.obtenerPorId(id) ?: return@launch
                val actualizado = usuario.copy(estadoUsuario = Estados.DESHABILITADO)
                repository.actualizar(actualizado)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    suspend fun obtenerPorId(id: Int): Usuario? = repository.obtenerPorId(id)
}



