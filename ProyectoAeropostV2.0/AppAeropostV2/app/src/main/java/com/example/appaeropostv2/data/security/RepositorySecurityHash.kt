package com.example.appaeropostv2.data.security

import android.util.Base64
import com.example.appaeropostv2.core.security.SecurityConstants
import com.example.appaeropostv2.domain.security.InterfaceSecurityHash
import com.example.appaeropostv2.domain.security.PasswordHash
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Implementación real del InterfaceSecurityHash usando PBKDF2WithHmacSHA256.
 *
 * - Aplica salt + pepper.
 * - Usa un número seguro de iteraciones.
 * - Comparación en tiempo constante.
 */
class RepositorySecurityHash : InterfaceSecurityHash {

    private val secureRandom = SecureRandom()

    private companion object {
        const val DEFAULT_ITERATIONS = 65_536
        const val KEY_LENGTH_BITS = 256
        const val SALT_SIZE_BYTES = 16
    }

    /** Genera un salt fuerte aleatorio */
    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_SIZE_BYTES)
        secureRandom.nextBytes(salt)
        return salt
    }

    /** Aplica PBKDF2 con SHA256 */
    private fun pbkdf2(
        password: CharArray,
        salt: ByteArray,
        iterations: Int,
        keyLengthBits: Int
    ): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, keyLengthBits)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec).encoded
    }

    override fun hashPassword(plainPassword: String): PasswordHash {
        // Pepper: no se guarda en BD.
        val passwordWithPepper = (plainPassword + SecurityConstants.PASSWORD_PEPPER)
        val salt = generateSalt()

        val hash = pbkdf2(
            password = passwordWithPepper.toCharArray(),
            salt = salt,
            iterations = DEFAULT_ITERATIONS,
            keyLengthBits = KEY_LENGTH_BITS
        )

        return PasswordHash(
            hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP),
            saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP),
            iterations = DEFAULT_ITERATIONS
        )
    }

    override fun verifyPassword(
        plainPassword: String,
        stored: PasswordHash
    ): Boolean {
        val salt = Base64.decode(stored.saltBase64, Base64.NO_WRAP)
        val expectedHash = Base64.decode(stored.hashBase64, Base64.NO_WRAP)

        val passwordWithPepper = (plainPassword + SecurityConstants.PASSWORD_PEPPER)

        val candidateHash = pbkdf2(
            password = passwordWithPepper.toCharArray(),
            salt = salt,
            iterations = stored.iterations,
            keyLengthBits = expectedHash.size * 8
        )

        // Comparación segura (tiempo constante)
        if (candidateHash.size != expectedHash.size) return false

        var diff = 0
        for (i in candidateHash.indices) {
            diff = diff or (candidateHash[i].toInt() xor expectedHash[i].toInt())
        }

        return diff == 0
    }
}
