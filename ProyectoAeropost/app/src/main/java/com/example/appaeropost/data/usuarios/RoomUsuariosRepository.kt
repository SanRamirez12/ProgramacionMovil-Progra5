package com.example.appaeropost.data.usuarios.repository

import com.example.appaeropost.data.repository.UsuariosRepository
import com.example.appaeropost.data.usuarios.db.UsuariosDao
import com.example.appaeropost.data.usuarios.db.toDomain
import com.example.appaeropost.data.usuarios.db.toEntity
import com.example.appaeropost.domain.EstadoUsuario
import com.example.appaeropost.domain.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RoomUsuariosRepository(
    private val dao: UsuariosDao
) : UsuariosRepository {

    // (Puedes luego migrar a stateIn; por ahora mantenemos MutableStateFlow para probar)
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    override val usuarios: StateFlow<List<Usuario>> = _usuarios

    override suspend fun crear(usuario: Usuario): Result<Unit> = runCatching {
        dao.insertar(usuario.toEntity())
    }

    override suspend fun actualizar(username: String, cambios: (Usuario) -> Usuario): Result<Unit> = runCatching {
        val actual = dao.obtenerPorUsername(username)?.toDomain()
            ?: throw NoSuchElementException("Usuario no encontrado")
        dao.actualizar(cambios(actual).toEntity())
    }

    override suspend fun eliminar(username: String): Result<Unit> = runCatching {
        dao.eliminar(username)
    }

    override suspend fun habilitar(username: String, habilitar: Boolean): Result<Unit> = runCatching {
        val usuario = dao.obtenerPorUsername(username)?.toDomain()
            ?: throw NoSuchElementException("Usuario no encontrado")
        val nuevoEstado = if (habilitar) EstadoUsuario.HABILITADO else EstadoUsuario.DESHABILITADO
        val actualizado = usuario.conEstado(nuevoEstado)
        dao.actualizar(actualizado.toEntity())
    }

    override suspend fun obtenerPorUsername(username: String): Usuario? =
        dao.obtenerPorUsername(username)?.toDomain()

    override suspend fun obtenerPorCedula(cedula: String): Usuario? =
        dao.obtenerPorCedula(cedula)?.toDomain()

    override suspend fun buscar(query: String): List<Usuario> =
        dao.buscar(query).map { it.toDomain() }

    override suspend fun existeUsername(username: String): Boolean =
        dao.obtenerPorUsername(username) != null

    override suspend fun autenticar(username: String, password: String): Boolean {
        val u = dao.obtenerPorUsername(username)?.toDomain() ?: return false
        return u.password == password && u.estadoUsuario == EstadoUsuario.HABILITADO
    }
}

