package com.example.appaeropostv2.core.security

/**
 * Constantes de seguridad globales.
 *
 * Un "pepper" es como un segundo salt, pero global para toda la app,
 * que se concatena a la contraseña antes de hashear.
 * A diferencia del salt, NO se guarda en la BD.
 */
object SecurityConstants {
    // OJO: en una app real esto debería venir de un lugar más seguro,
    // pero para el proyecto está bien así.
    const val PASSWORD_PEPPER = "AEROPOST_PEPPER_SUPER_SECRET_2025"
}
