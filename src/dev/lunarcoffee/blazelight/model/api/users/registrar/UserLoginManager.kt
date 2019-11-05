package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.std.util.PasswordHasher
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq

object UserLoginManager {
    suspend fun tryLogin(username: String, password: String): UserLoginResult {
        val user = Database.userCol.findOne(User::username eq username)
            ?: return UserLoginFailure

        return if (PasswordHasher(password, user.passwordSalt).hash() == user.passwordHash)
            UserLoginSuccess(user.id)
        else
            UserLoginFailure
    }
}
