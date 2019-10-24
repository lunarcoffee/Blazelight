package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import kotlinx.coroutines.runBlocking

fun Long.getUser(): User? {
    return UserRegistrar.users.find { this == it.id }
        ?: runBlocking { UserRegistrar.cacheFromDB(this@getUser) }
}

fun UserSession.getUser() = id.getUser()
