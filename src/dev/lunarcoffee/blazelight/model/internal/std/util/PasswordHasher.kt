package dev.lunarcoffee.blazelight.model.internal.std.util

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PasswordHasher(private val string: String, private val salt: ByteArray) {
    fun hash(): String {
        val keySpec = PBEKeySpec(string.toCharArray(), salt, 65_536, 128)
        return SecretKeyFactory
            .getInstance("PBKDF2WithHmacSHA1")
            .generateSecret(keySpec)
            .encoded
            .joinToString { it.toString(16).replace('-', '0') }
    }
}
