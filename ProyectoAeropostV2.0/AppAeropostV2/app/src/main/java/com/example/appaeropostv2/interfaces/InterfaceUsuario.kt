package com.example.appaeropostv2.interfaces

import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface InterfaceUsuario {

    fun observarUsuarios(): Flow<List<Usuario>>

    suspend fun obtenerPorId(idUsuario: Int): Usuario?
    suspend fun obtenerPorUsername(username: String): Usuario?
    suspend fun obtenerPorCedula(cedula: String): Usuario?

    suspend fun buscarPorNombreOUsuario(texto: String): List<Usuario>

    suspend fun validarLogin(
        username: String,
        password: String,
        estadoRequerido: Estados = Estados.HABILITADO
    ): Usuario?

    // AHORA NECESITA LA CONTRASEÑA PLANA
    suspend fun insertar(usuario: Usuario, plainPassword: String): Int

    suspend fun actualizar(usuario: Usuario)
    suspend fun eliminar(usuario: Usuario)

    suspend fun upsert(usuario: Usuario, plainPassword: String)
    suspend fun contar(): Int
}
