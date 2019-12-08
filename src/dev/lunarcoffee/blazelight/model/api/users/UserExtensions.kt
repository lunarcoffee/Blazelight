package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import kotlinx.coroutines.runBlocking

fun UserSession.getUser() = id.getUser()

fun Long.getUser(): User? {
    return UserCache.users.find { this == it.id }
        ?: runBlocking { UserCache.cacheFromDB(this@getUser) }
}

fun String.getUser(): User? {
    return UserCache.users.find { this == it.username }
        ?: runBlocking { UserCache.cacheFromDB(this@getUser) }
}
