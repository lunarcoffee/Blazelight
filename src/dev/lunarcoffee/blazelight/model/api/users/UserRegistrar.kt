package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.model.internal.util.PasswordHasher
import org.litote.kmongo.eq
import java.security.SecureRandom

object UserRegistrar {
    private val srng = SecureRandom()
    private val emailRegex = """[a-zA-Z0-9+.]+@[a-zA-Z0-9.]+""".toRegex() // TODO: make this good!

    // TODO: strip bad chars from name (extract to function (ext?)?).
    suspend fun tryRegister(email: String, name: String, password: String): UserRegisterResult {
        if (!(email matches emailRegex))
            return UserRegisterInvalidEmail
        if (name.length !in 1..40)
            return UserRegisterInvalidName
        if (password.length !in 8..200)
            return UserRegisterInvalidPassword
        if (Database.userCol.findOne(User::email eq email) != null)
            return UserRegisterDuplicateEmail
        if (Database.userCol.findOne(User::username eq name) != null)
            return UserRegisterDuplicateName

        val salt = ByteArray(16)
        srng.nextBytes(salt)
        val passwordHash = PasswordHasher(password, salt).hash()

        val user = DefaultUser(name, email, passwordHash, salt)
        Database.userCol.insertOne(user)

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

    suspend fun getUserById(id: Long) = Database.userCol.findOne(User::id eq id)
}
