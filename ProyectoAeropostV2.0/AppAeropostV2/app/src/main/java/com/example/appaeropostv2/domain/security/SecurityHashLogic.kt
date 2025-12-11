package com.example.appaeropostv2.domain.security

import android.util.Base64
import com.example.appaeropostv2.core.security.SecurityConstants
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Versión utilitaria del hash, aunque ahora el ViewModel debería usar InterfaceSecurityHash.
 *
 * La dejo porque puede servir para utilidades internas,
 * pero la implementación recomendada es RepositorySecurityHash.
 */
object SecurityHashLogic {

    private const val ITER = 65_536
    private const val KEY_BITS = 256
    private const val SALT_BYTES = 16

    private val random = SecureRandom()

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_BYTES)
        random.nextBytes(salt)
        return salt
    }

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

    fun hashPassword(plainPassword: String): PasswordHash {
        val peppered = plainPassword + SecurityConstants.PASSWORD_PEPPER
        val salt = generateSalt()

        val hash = pbkdf2(
            peppered.toCharArray(),
            salt,
            ITER,
            KEY_BITS
        )

        return PasswordHash(
            hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP),
            saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP),
            iterations = ITER
        )
    }

    fun verifyPassword(
        plainPassword: String,
        stored: PasswordHash
    ): Boolean {
        val peppered = plainPassword + SecurityConstants.PASSWORD_PEPPER

        val salt = Base64.decode(stored.saltBase64, Base64.NO_WRAP)
        val expectedHash = Base64.decode(stored.hashBase64, Base64.NO_WRAP)

        val candidate = pbkdf2(
            password = peppered.toCharArray(),
            salt = salt,
            iterations = stored.iterations,
            keyLengthBits = expectedHash.size * 8
        )

        if (candidate.size != expectedHash.size) return false

        var diff = 0
        for (i in candidate.indices) {
            diff = diff or (candidate[i].toInt() xor expectedHash[i].toInt())
        }

        return diff == 0
    }
}
