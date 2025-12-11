package com.example.appaeropostv2.domain.security

/**
 * Estructura que representa los datos necesarios para validar una contraseña.
 *
 * Se guarda en BD como:
 * - hashBase64
 * - saltBase64
 * - iterations
 *
 * NO se guarda la contraseña en claro.
 * NO se puede revertir.
 */
data class PasswordHash(
    val hashBase64: String,
    val saltBase64: String,
    val iterations: Int
)
