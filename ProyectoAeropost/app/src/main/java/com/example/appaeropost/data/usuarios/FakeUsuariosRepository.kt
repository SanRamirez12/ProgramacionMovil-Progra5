package com.example.appaeropost.data.repository

import com.example.appaeropost.domain.EstadoUsuario
import com.example.appaeropost.domain.Genero
import com.example.appaeropost.domain.Rol
import com.example.appaeropost.domain.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate

/**
 * Implementación en memoria para desarrollo/QA.
 * Reglas mínimas:
 *  - username y cédula únicos
 *  - habilitar/deshabilitar cambia EstadoUsuario
 */
class FakeUsuariosRepository : UsuariosRepository {

    private val lock = Mutex()
    private val _usuarios = MutableStateFlow<List<Usuario>>(seed())
    override val usuarios: StateFlow<List<Usuario>> = _usuarios

    override suspend fun crear(usuario: Usuario): Result<Unit> = lock.withLock {
        val lista = _usuarios.value
        if (lista.any { it.username.equals(usuario.username, true) })
            return Result.failure(IllegalArgumentException("El username ya existe"))
        if (lista.any { it.cedula == usuario.cedula })
            return Result.failure(IllegalArgumentException("La cédula ya existe"))

        _usuarios.value = lista + usuario.copy(
            // Aseguramos fecha de registro por si viene vacía en llamadas futuras
            fechaRegistro = usuario.fechaRegistro
        )
        Result.success(Unit)
    }

    override suspend fun actualizar(username: String, cambios: (Usuario) -> Usuario): Result<Unit> =
        lock.withLock {
            val lista = _usuarios.value
            val idx = lista.indexOfFirst { it.username.equals(username, true) }
            if (idx == -1) return Result.failure(NoSuchElementException("Usuario no encontrado"))
            val actualizado = cambios(lista[idx])

            // Validar duplicados si cambian username/cedula
            val dupUser = lista
                .filterIndexed { i, _ -> i != idx }
                .any { it.username.equals(actualizado.username, true) }
            if (dupUser) return Result.failure(IllegalArgumentException("El username ya existe"))

            val dupCed = lista
                .filterIndexed { i, _ -> i != idx }
                .any { it.cedula == actualizado.cedula }
            if (dupCed) return Result.failure(IllegalArgumentException("La cédula ya existe"))

            _usuarios.value = lista.toMutableList().also { it[idx] = actualizado }
            Result.success(Unit)
        }

    override suspend fun eliminar(username: String): Result<Unit> = lock.withLock {
        val lista = _usuarios.value
        if (lista.none { it.username.equals(username, true) })
            return Result.failure(NoSuchElementException("Usuario no encontrado"))
        _usuarios.value = lista.filterNot { it.username.equals(username, true) }
        Result.success(Unit)
    }

    override suspend fun habilitar(username: String, habilitar: Boolean): Result<Unit> =
        actualizar(username) { u ->
            u.conEstado(if (habilitar) EstadoUsuario.HABILITADO else EstadoUsuario.DESHABILITADO)
        }

    override suspend fun obtenerPorUsername(username: String): Usuario? =
        _usuarios.value.firstOrNull { it.username.equals(username, true) }

    override suspend fun obtenerPorCedula(cedula: String): Usuario? =
        _usuarios.value.firstOrNull { it.cedula == cedula }

    override suspend fun buscar(query: String): List<Usuario> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return _usuarios.value
        return _usuarios.value.filter { u ->
            u.username.lowercase().contains(q) ||
                    u.nombre.lowercase().contains(q) ||
                    u.cedula.lowercase().contains(q) ||
                    u.correo.lowercase().contains(q) ||
                    u.rol.name.lowercase().contains(q)
        }
    }

    override suspend fun existeUsername(username: String): Boolean =
        _usuarios.value.any { it.username.equals(username, true) }

    override suspend fun autenticar(username: String, password: String): Boolean {
        val u = obtenerPorUsername(username) ?: return false
        // En real: comparar hash. Aquí: texto plano por ser FakeRepo.
        return u.password == password && u.estaHabilitado
    }

    private fun seed(): List<Usuario> = listOf(
        Usuario(
            nombre = "Admin General",
            cedula = "101010101",
            genero = Genero.NO_DECLARA,
            fechaRegistro = LocalDate.now(),
            estado = EstadoUsuario.HABILITADO,
            rol = Rol.ADMINISTRADOR,
            correo = "admin@aeropostapp.com",
            username = "admin",
            password = "admin123"
        ),
        Usuario(
            nombre = "Santiago Ramírez",
            cedula = "202401112941",
            genero = Genero.MASCULINO,
            fechaRegistro = LocalDate.now(),
            estado = EstadoUsuario.HABILITADO,
            rol = Rol.SUPERVISOR,
            correo = "santiago@example.com",
            username = "sramirez",
            password = "123456"
        ),
        Usuario(
            nombre = "Operador Demo",
            cedula = "303030303",
            genero = Genero.OTRO,
            fechaRegistro = LocalDate.now(),
            estado = EstadoUsuario.DESHABILITADO,
            rol = Rol.OPERADOR,
            correo = "operador@example.com",
            username = "operador1",
            password = "operador"
        ),
        Usuario(
            nombre = "Courier Demo",
            cedula = "404040404",
            genero = Genero.FEMENINO,
            fechaRegistro = LocalDate.now(),
            estado = EstadoUsuario.HABILITADO,
            rol = Rol.COURIER,
            correo = "courier@example.com",
            username = "courier1",
            password = "courier"
        )
    )
}
