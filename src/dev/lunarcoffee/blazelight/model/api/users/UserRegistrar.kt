package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import dev.lunarcoffee.blazelight.model.internal.std.util.PasswordHasher
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.shared.language.Language
import org.litote.kmongo.eq
import org.litote.kmongo.setValue
import java.security.SecureRandom
import java.time.ZoneId

object UserRegistrar : DBCacheable<User> {
    private val srng = SecureRandom()

    private val EMAIL_REGEX = "[a-zA-Z0-9+.]+@[a-zA-Z0-9.]+".toRegex()
    private val ILLEGAL_CHARS_REGEX = "[\u0000-\u001F\u007F-\u009F\u200B-\u200F\u2060\uFEFF]"
        .toRegex()

    val users = Cache<User>()

    suspend fun tryRegister(
        email: String,
        name: String,
        password: String,
        timeZone: ZoneId,
        language: Language
    ): UserRegisterResult {

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

        val salt = ByteArray(16)
        srng.nextBytes(salt)
        val passwordHash = PasswordHasher(password, salt).hash()

        val user = DefaultUser(name, email, passwordHash, salt, timeZone, language)
        Database.userCol.insertOne(user)
        users += user

        return UserRegisterSuccess
    }

    suspend fun tryLogin(username: String, password: String): UserLoginResult {
        val user = Database.userCol.findOne(User::username eq username)
            ?: return UserLoginFailure

        return if (PasswordHasher(password, user.passwordSalt).hash() == user.passwordHash)
            UserLoginSuccess(user.id)
        else
            UserLoginFailure
    }

    suspend fun addComment(commentId: Long, userId: Long) {
        val newCommentIds = userId.getUser()!!.commentIds + commentId
        Database.userCol.updateOne(User::id eq userId, setValue(User::commentIds, newCommentIds))
        cacheFromDB(userId)
    }

    suspend fun addThread(threadId: Long, userId: Long) {
        val newThreadIds = userId.getUser()!!.threadIds + threadId
        Database.userCol.updateOne(User::id eq userId, setValue(User::threadIds, newThreadIds))
        cacheFromDB(userId)
    }

    override suspend fun cacheFromDB(id: Long): User? {
        return Database.userCol.findOne(User::id eq id)?.also { users += it }
    }

    private fun String.sanitize() = replace(ILLEGAL_CHARS_REGEX, "").trim()
}
