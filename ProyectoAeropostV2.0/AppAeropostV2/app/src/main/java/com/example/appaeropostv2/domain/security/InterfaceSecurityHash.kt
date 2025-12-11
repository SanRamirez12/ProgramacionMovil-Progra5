package com.example.appaeropostv2.domain.security

/**
 * Contrato de seguridad para hashing y verificación de contraseñas.
 *
 * Esto permite:
 * - Testear más fácilmente.
 * - Sustituir PBKDF2 por Argon2/bcrypt en el futuro sin cambiar ViewModels.
 * - Evitar acoplar RepositoryUsuario directamente al SecurityHashLogic.
 */
interface InterfaceSecurityHash {

    /**
     * Genera un hash seguro usando:
     * - Contraseña en texto plano
     * - Salt aleatorio
     * - Pepper global
     * - Iteraciones PBKDF2
     */
    fun hashPassword(plainPassword: String): PasswordHash

    /**
     * Verifica si una contraseña ingresada coincide con el hash almacenado.
     */
    fun verifyPassword(
        plainPassword: String,
        stored: PasswordHash
    ): Boolean
}
