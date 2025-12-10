package com.example.appaeropostv2.domain.logic

import java.security.MessageDigest

/**
 * Lógica de generación del "sello digital" de la factura.
 *
 * Está en domain porque es parte de la lógica de negocio,
 * no depende de Android ni de la UI.
 */
object FacturaSecurityLogic {

    /**
     * Genera un sello digital simple a partir de una cadena de entrada.
     * Usa SHA-256 y devuelve los primeros 32 caracteres hexadecimales.
     */
    fun generarSelloDigital(entrada: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(entrada.toByteArray())

        return digest.joinToString("") { byte -> "%02x".format(byte) }.take(32).uppercase()
    }
}
