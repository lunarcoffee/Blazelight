package dev.lunarcoffee.blazelight.model.api.users.registrar

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq

object UserEditManager {
    suspend fun edit(user: User) {
        UserCache.users.removeIf { it.id == user.id }
        UserCache.users += user
        Database.userCol.replaceOne(User::id eq user.id, user as DefaultUser)
    }
}
