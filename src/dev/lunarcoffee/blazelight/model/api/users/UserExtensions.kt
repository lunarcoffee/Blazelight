package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.site.sessions.UserSession

suspend fun UserSession.getUser() = UserRegistrar.getUserById(id)!!
