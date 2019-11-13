package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq

object UserDeleteManager {
    const val DELETED_USER_NAME = "[deleted]\u200B"

    suspend fun delete(user: User) {
        UserCache.users.removeIf { it.id == user.id }
        Database.userCol.deleteOne(User::id eq user.id)

        // Add a placeholder user.
        UserRegisterManager.registerDeleted(user, DELETED_USER_NAME)
    }
}
