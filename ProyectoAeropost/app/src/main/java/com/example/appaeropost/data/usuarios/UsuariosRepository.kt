package com.example.appaeropost.data.repository

import com.example.appaeropost.domain.Usuario
import kotlinx.coroutines.flow.StateFlow

/**
 * Contrato del repositorio de Usuarios.
 * Implementaciones: Fake (en memoria) ahora; Room/Remote más adelante.
 */
interface UsuariosRepository {
    /** Flujo observable de la lista completa (útil para Compose) */
    val usuarios: StateFlow<List<Usuario>>

    suspend fun crear(usuario: Usuario): Result<Unit>
    suspend fun actualizar(username: String, cambios: (Usuario) -> Usuario): Result<Unit>
    suspend fun eliminar(username: String): Result<Unit>

    suspend fun habilitar(username: String, habilitar: Boolean): Result<Unit>

    suspend fun obtenerPorUsername(username: String): Usuario?
    suspend fun obtenerPorCedula(cedula: String): Usuario?
    suspend fun buscar(query: String): List<Usuario>

    suspend fun existeUsername(username: String): Boolean
    suspend fun autenticar(username: String, password: String): Boolean
}
