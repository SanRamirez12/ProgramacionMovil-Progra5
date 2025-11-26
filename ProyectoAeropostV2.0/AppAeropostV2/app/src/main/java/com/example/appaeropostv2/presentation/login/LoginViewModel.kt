package com.example.appaeropostv2.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appaeropostv2.core.session.SessionManager
import com.example.appaeropostv2.data.repository.RepositoryBitacora
import com.example.appaeropostv2.data.repository.RepositoryUsuario
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Bitacora
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isAdminLogin: Boolean = false,   // lo dejamos por compatibilidad con la UI
    val isHostAdmin: Boolean = false,
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val repositoryUsuario: RepositoryUsuario,
    private val repositoryBitacora: RepositoryBitacora
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    // ───────────────────────── helpers de estado UI ─────────────────────────

    fun onUsernameChange(newValue: String) {
        uiState = uiState.copy(username = newValue, errorMessage = null)
    }

    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue, errorMessage = null)
    }

    fun onHostAdminToggle(checked: Boolean) {
        uiState = uiState.copy(isHostAdmin = checked, errorMessage = null)
    }

    fun onAdminLoginToggle(checked: Boolean) {
        uiState = uiState.copy(isAdminLogin = checked, errorMessage = null)
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(passwordVisible = !uiState.passwordVisible)
    }

    // ───────────────────────── lógica de login ─────────────────────────

    fun login(onSuccess: () -> Unit) {
        val user = uiState.username.trim()
        val pass = uiState.password

        if (user.isBlank() || pass.isBlank()) {
            uiState = uiState.copy(errorMessage = "Por favor, ingrese usuario y contraseña.")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // 1) Host admin (backdoor) SIEMPRE disponible con estas credenciales
                if (user == "AdminKing12" && pass == "1205") {
                    // No hay usuario en BD, pero dejamos la sesión sin usuario
                    // (Home usará 'Master' como nombre cuando no haya currentUser)
                    SessionManager.setCurrentUser(null)

                    registrarLoginEnBitacora("AdminKing12")

                    uiState = uiState.copy(isLoading = false, errorMessage = null)
                    onSuccess()
                    return@launch
                }

                // 2) Login normal contra la tabla Usuario
                val usuario = repositoryUsuario.validarLogin(
                    username = user,
                    password = pass,
                    estadoRequerido = Estados.HABILITADO
                )

                if (usuario == null) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Credenciales inválidas o usuario deshabilitado."
                    )
                } else {
                    // Guardamos usuario en memoria
                    SessionManager.setCurrentUser(usuario)

                    // Logout automático de la sesión anterior (si la hay) e inserción del nuevo login
                    registrarLoginEnBitacora(usuario.username)

                    uiState = uiState.copy(isLoading = false, errorMessage = null)
                    onSuccess()
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ocurrió un error al iniciar sesión."
                )
            }
        }
    }

    // ───────────────────────── Bitácora (logout automático) ─────────────────────────

    private suspend fun registrarLoginEnBitacora(username: String) {
        val ahoraIso = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Cierra la sesión anterior de ese usuario (si existe registro con logout vacío)
        repositoryBitacora.cerrarSesionUsuario(
            username = username,
            logoutIso  = ahoraIso
        )

        // Inserta el nuevo registro de login (logout vacío por ahora)
        val nuevoRegistro = Bitacora(
            idBitacora = 0,              // Room autogenera
            username = username,
            fechaLogin = ahoraIso,
            fechaLogout = ""
        )
        repositoryBitacora.insertar(nuevoRegistro)
    }
}

// ───────────────────────── Factory para NavGraph ─────────────────────────

class LoginViewModelFactory(
    private val repositoryUsuario: RepositoryUsuario,
    private val repositoryBitacora: RepositoryBitacora
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repositoryUsuario, repositoryBitacora) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}


