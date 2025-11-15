package com.example.appaeropostv2.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(newValue: String) {
        uiState = uiState.copy(username = newValue, errorMessage = null)
    }

    fun onPasswordChange(newValue: String) {
        uiState = uiState.copy(password = newValue, errorMessage = null)
    }

    /**
     * Host "backdoor" para entrar siempre al sistema aunque no exista en BD.
     *
     * Username: AdminKing12
     * Password: 1205
     */
    fun isHostAdmin(username: String, password: String): Boolean {
        return username == "AdminKing12" && password == "1205"
    }

    /**
     * Lógica de login.
     * De momento:
     *  - Si coincide con el host, entra directo.
     *  - El resto queda como TODO para cuando conectes Room / API.
     */
    fun login(onSuccess: () -> Unit) {
        val user = uiState.username.trim()
        val pass = uiState.password

        if (user.isBlank() || pass.isBlank()) {
            uiState = uiState.copy(
                errorMessage = "Por favor completa usuario y contraseña."
            )
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        // 1) Host admin (backdoor)
        if (isHostAdmin(user, pass)) {
            uiState = uiState.copy(isLoading = false)
            onSuccess()
            return
        }

        // 2) TODO: aquí luego llamas a tu repositorio/Room/servicio de login real
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = "Credenciales inválidas (demo sin backend)."
        )
    }
}
