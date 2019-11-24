package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.std.util.PasswordHasher
import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator
import dev.lunarcoffee.blazelight.model.internal.users.*
import dev.lunarcoffee.blazelight.model.internal.users.im.UserIMDataList
import dev.lunarcoffee.blazelight.shared.language.Language
import dev.lunarcoffee.blazelight.shared.sanitize
import org.litote.kmongo.eq
import java.security.SecureRandom
import java.time.ZoneId

object UserRegisterManager {
    private val srng = SecureRandom()
    private val EMAIL_REGEX = "[a-zA-Z0-9+.]+@[a-zA-Z0-9.]+".toRegex()

    suspend fun tryRegister(
        email: String,
        name: String,
        password: String,
        timeZone: ZoneId,
        language: Language
    ): UserRegisterResult {

        if (!(email matches EMAIL_REGEX))
            return UserRegisterResult.INVALID_EMAIL
        if (name.length !in 1..40 || name.sanitize() != name)
            return UserRegisterResult.INVALID_NAME
        if (password.length !in 8..1_000 || password.sanitize() != password)
            return UserRegisterResult.INVALID_PASSWORD
        if (Database.userCol.findOne(User::email eq email) != null)
            return UserRegisterResult.DUPLICATE_EMAIL
        if (Database.userCol.findOne(User::username eq name) != null)
            return UserRegisterResult.DUPLICATE_USERNAME

        val salt = ByteArray(16)
        srng.nextBytes(salt)
        val passwordHash = PasswordHasher(password, salt).hash()

        val settings = UserSettings(UniqueIDGenerator.nextId(), timeZone, language)
        val imDataList = UserIMDataList(settings.userId) // TODO
        val user = DefaultUser(name, email, passwordHash, salt, imDataList, settings)
        Database.userCol.insertOne(user)
        UserCache.users += user

        return UserRegisterResult.SUCCESS
    }

    suspend fun registerDeleted(copyUser: User, deleted: String) {
        val user = DeletedDefaultUser(copyUser, deleted)
        Database.userCol.insertOne(user)
        UserCache.users += user
    }
}
