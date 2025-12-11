package com.example.appaeropostv2.data.repository

import com.example.appaeropostv2.data.local.dao.DaoUsuario
import com.example.appaeropostv2.data.mappers.toDomain
import com.example.appaeropostv2.data.mappers.toEntity
import com.example.appaeropostv2.domain.enums.Estados
import com.example.appaeropostv2.domain.model.Usuario
import com.example.appaeropostv2.domain.security.InterfaceSecurityHash
import com.example.appaeropostv2.domain.security.PasswordHash
import com.example.appaeropostv2.interfaces.InterfaceUsuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryUsuario(
    private val dao: DaoUsuario,
    private val securityHash: InterfaceSecurityHash
) : InterfaceUsuario {

    override fun observarUsuarios(): Flow<List<Usuario>> =
        dao.observarUsuarios().map { list -> list.map { it.toDomain() } }

    override suspend fun obtenerPorId(idUsuario: Int): Usuario? =
        dao.obtenerPorId(idUsuario)?.toDomain()

    override suspend fun obtenerPorUsername(username: String): Usuario? =
        dao.obtenerPorUsername(username)?.toDomain()

    override suspend fun obtenerPorCedula(cedula: String): Usuario? =
        dao.obtenerPorCedula(cedula)?.toDomain()

    override suspend fun buscarPorNombreOUsuario(texto: String): List<Usuario> {
        val patron = "%${texto}%"
        return dao.buscarPorNombreOUsuario(patron).map { it.toDomain() }
    }

    override suspend fun validarLogin(
        username: String,
        password: String,
        estadoRequerido: Estados
    ): Usuario? {
        val entity = dao.obtenerPorUsername(username) ?: return null

        // Primero verificamos el estado
        if (entity.estadoUsuario != estadoRequerido) return null

        // Construimos PasswordHash a partir de los campos de la entidad
        val storedHash = PasswordHash(
            hashBase64 = entity.passwordHash,
            saltBase64 = entity.passwordSalt,
            iterations = entity.passwordIterations
        )

        val ok = securityHash.verifyPassword(password, storedHash)
        return if (ok) entity.toDomain() else null
    }

    override suspend fun insertar(usuario: Usuario, plainPassword: String): Int {
        // Generamos el hash con pepper + salt
        val hashData = securityHash.hashPassword(plainPassword)

        val entity = usuario.copy(
            passwordHash = hashData.hashBase64,
            passwordSalt = hashData.saltBase64,
            passwordIterations = hashData.iterations
        ).toEntity()

        return dao.insert(entity).toInt()
    }

    override suspend fun actualizar(usuario: Usuario) {
        // Aquí asumimos que el usuario ya viene con sus campos de hash listos.
        // Si quisieras cambiar contraseña, se haría en otro método especial.
        dao.update(usuario.toEntity())
    }

    override suspend fun eliminar(usuario: Usuario) {
        dao.delete(usuario.toEntity())
    }

    override suspend fun upsert(usuario: Usuario, plainPassword: String) {
        if (usuario.idUsuario == 0) {
            insertar(usuario, plainPassword)
        } else {
            actualizar(usuario)
        }
    }

    override suspend fun contar(): Int = dao.contar()
}
