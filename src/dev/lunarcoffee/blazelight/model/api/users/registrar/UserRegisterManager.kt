package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.std.util.PasswordHasher
import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator
import dev.lunarcoffee.blazelight.model.internal.users.*
import dev.lunarcoffee.blazelight.shared.language.Language
import org.litote.kmongo.eq
import java.security.SecureRandom
import java.time.ZoneId

object UserRegisterManager {
    private val srng = SecureRandom()

    private val EMAIL_REGEX = "[a-zA-Z0-9+.]+@[a-zA-Z0-9.]+".toRegex()
    private val ILLEGAL_CHARS_REGEX = "[\u0000-\u001F\u007F-\u009F\u200B-\u200F\u2060\uFEFF]"
        .toRegex()

    suspend fun tryRegister(
        email: String,
        name: String,
        password: String,
        timeZone: ZoneId,
        language: Language,
        bypassChecks: Boolean = false, // This and the following are used in user deletion.
        forceId: Long = UniqueIDGenerator.nextId()
    ): UserRegisterResult {

        if (!bypassChecks) {
            if (!(email matches EMAIL_REGEX))
                return UserRegisterInvalidEmail
            if (name.length !in 1..40 || name.sanitize() != name)
                return UserRegisterInvalidName
            if (password.length !in 8..1_000 || password.sanitize() != password)
                return UserRegisterInvalidPassword
            if (Database.userCol.findOne(User::email eq email) != null)
                return UserRegisterDuplicateEmail
            if (Database.userCol.findOne(User::username eq name) != null)
                return UserRegisterDuplicateName
        }

        val salt = ByteArray(16)
        srng.nextBytes(salt)
        val passwordHash = PasswordHasher(password, salt).hash()

        val settings = UserSettings(timeZone, language, forceId)
        val user = DefaultUser(name, email, passwordHash, salt, settings)
        Database.userCol.insertOne(user)
        UserCache.users += user

        return UserRegisterSuccess
    }

    private fun String.sanitize() = replace(ILLEGAL_CHARS_REGEX, "").trim()
}
