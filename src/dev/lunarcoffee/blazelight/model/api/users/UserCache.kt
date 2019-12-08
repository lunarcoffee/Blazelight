package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq

object UserCache : DBCacheable<User> {
    val users = Cache<User>()

    override suspend fun cacheFromDB(id: Long): User? {
        return Database.userCol.findOne(User::id eq id)?.also { users += it }
    }

    suspend fun cacheFromDB(username: String): User? {
        return Database.userCol.findOne(User::username eq username)?.also { users += it }
    }
}
