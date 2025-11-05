package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoUsuario
import com.example.appaeropostv2.data.mappers.toDomain
import com.example.appaeropostv2.data.mappers.toEntity
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.interfaces.InterfaceUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta que usa Room por debajo.
 * - Depende del DaoUsuario y mapea Entities ↔ Domain.
 */
class RepositoryUsuario(
    private val dao: DaoUsuario
) : InterfaceUsuario {

    override fun observarUsuarios(): Flow<List<Usuario>> =
        dao.observarUsuarios().map { list -> list.map { it.toDomain() } }

    override suspend fun obtenerPorId(idUsuario: Int): Usuario? =
        dao.obtenerPorId(idUsuario)?.toDomain()

    override suspend fun obtenerPorUsername(username: String): Usuario? =
        dao.obtenerPorUsername(username)?.toDomain()

    override suspend fun obtenerPorCedula(cedula: String): Usuario? =
        dao.obtenerPorCedula(cedula)?.toDomain()

    override suspend fun buscarPorNombreOUsuario(texto: String): List<Usuario> =
        dao.buscarPorNombreOUsuario("%$texto%").map { it.toDomain() }

    override suspend fun validarLogin(
        username: String,
        password: String,
        estadoRequerido: Estados
    ): Usuario? = dao.validarLogin(username, password, estadoRequerido)?.toDomain()

    override suspend fun insertar(usuario: Usuario): Int =
        dao.insert(usuario.toEntity()).toInt()

    override suspend fun actualizar(usuario: Usuario) {
        dao.update(usuario.toEntity())
    }

    override suspend fun eliminar(usuario: Usuario) {
        dao.delete(usuario.toEntity())
    }

    override suspend fun upsert(usuario: Usuario) {
        dao.upsert(usuario.toEntity())
    }

    override suspend fun contar(): Int = dao.contar()
}
