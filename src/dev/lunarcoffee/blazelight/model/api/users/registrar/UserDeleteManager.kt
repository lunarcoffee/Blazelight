package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq

object UserDeleteManager {
    suspend fun delete(user: User) {
        UserCache.users.removeIf { it.id == user.id }
        Database.userCol.deleteOne(User::id eq user.id)

        // Add a placeholder user.
        val placeholderUser = UserRegisterManager.tryRegister(
            "[deleted]\u200B",
            "[deleted]\u200B",
            " ",
            user.settings.zoneId,
            user.settings.language,
            true,
            user.id
        )
        if (placeholderUser !is UserRegisterSuccess)
            error("Placeholder user registration in deletion failed!")
    }
}
