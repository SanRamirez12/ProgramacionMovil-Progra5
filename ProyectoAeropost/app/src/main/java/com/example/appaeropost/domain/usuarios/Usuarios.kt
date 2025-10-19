package com.example.appaeropost.domain

import java.time.LocalDate

/**
 * Modelo lógico de Usuario.
 * NOTA: Por ahora password es texto plano porque aún no implementamos persistencia.
 * En la fase de seguridad/infra se debe almacenar un hash seguro (p. ej. BCrypt/Argon2).
 */
data class Usuario(
    val nombre: String,
    val cedula: String,
    val genero: Genero,
    val fechaRegistro: LocalDate,
    val estado: EstadoUsuario,
    val rol: Rol,
    val correo: String,
    val username: String,
    val password: String
) {
    val estaHabilitado: Boolean get() = estado == EstadoUsuario.HABILITADO

    fun conEstado(nuevo: EstadoUsuario) = copy(estado = nuevo)
    fun conPassword(nuevoPassword: String) = copy(password = nuevoPassword)
}

/** Enums internos al dominio de Usuarios (requerido) */
enum class Genero { MASCULINO, FEMENINO, OTRO, NO_DECLARA }

enum class EstadoUsuario { HABILITADO, DESHABILITADO }

enum class Rol { ADMINISTRADOR, SUPERVISOR, OPERADOR, COURIER }
