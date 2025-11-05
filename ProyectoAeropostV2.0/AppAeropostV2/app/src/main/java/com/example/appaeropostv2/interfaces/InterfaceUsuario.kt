package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de alto nivel para gestionar usuarios desde la UI/casos de uso.
 * Trabaja SIEMPRE con el modelo de dominio (no con Entities de Room).
 */
interface InterfaceUsuario {

    // Observación reactiva (para listas en Compose)
    fun observarUsuarios(): Flow<List<Usuario>>

    // CRUD
    suspend fun obtenerPorId(idUsuario: Int): Usuario?
    suspend fun obtenerPorUsername(username: String): Usuario?
    suspend fun obtenerPorCedula(cedula: String): Usuario?

    suspend fun buscarPorNombreOUsuario(texto: String): List<Usuario>

    suspend fun validarLogin(
        username: String,
        password: String,
        estadoRequerido: Estados = Estados.HABILITADO
    ): Usuario?

    suspend fun insertar(usuario: Usuario): Int
    suspend fun actualizar(usuario: Usuario)
    suspend fun eliminar(usuario: Usuario)

    suspend fun upsert(usuario: Usuario)
    suspend fun contar(): Int
}
