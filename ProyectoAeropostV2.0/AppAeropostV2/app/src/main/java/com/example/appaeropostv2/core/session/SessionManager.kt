package com.example.appaeropostv2.core.session

import com.example.appaeropostv2.domain.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * SessionManager simple en memoria para guardar
 * el usuario actualmente logueado.
 */
object SessionManager {

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    fun setCurrentUser(usuario: Usuario?) {
        _currentUser.value = usuario
    }

    fun clearSession() {
        _currentUser.value = null
    }
}
