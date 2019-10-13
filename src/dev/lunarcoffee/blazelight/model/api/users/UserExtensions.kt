package dev.lunarcoffee.blazelight.model.api.users

import dev.lunarcoffee.blazelight.routes.sessions.UserSession

suspend fun UserSession.getUser() = UserRegistrar.getUserById(id)!!
