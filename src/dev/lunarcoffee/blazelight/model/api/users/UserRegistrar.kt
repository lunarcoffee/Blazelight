package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.util.PasswordHasher
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq
import java.security.SecureRandom

object UserRegistrar {
    private val srng = SecureRandom()

    suspend fun tryRegister(email: String, username: String, password: String) {
        val salt = ByteArray(16)
        srng.nextBytes(salt)
        val passwordHash = PasswordHasher(password, salt).hash()

        val user = DefaultUser(username, email, passwordHash, salt)
        Database.userCol.insertOne(user)
    }

    suspend fun tryLogin(username: String, password: String): UserLoginResult {
        val user = Database.userCol.findOne(User::username eq username)
            ?: return UserLoginResult.FAILURE

        if (PasswordHasher(password, user.passwordSalt).hash() == user.passwordHash)
            return UserLoginResult.SUCCESS
        return UserLoginResult.FAILURE
    }
}
