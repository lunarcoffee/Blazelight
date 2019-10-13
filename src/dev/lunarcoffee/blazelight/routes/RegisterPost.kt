package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.model.api.users.*
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.post

fun Routing.registerPostRoute() = post("/register") {
    val params = call.receiveParameters()

    val email = params["email"]!!
    val username = params["username"]!!
    val password = params["password"]!!

    // This will index into a list of special failure messages in [Routing.registerRoute].
    val specialMessageIndex = when (UserRegistrar.tryRegister(email, username, password)) {
        is UserRegisterInvalidEmail -> 0
        is UserRegisterInvalidName -> 1
        is UserRegisterInvalidPassword -> 2
        is UserRegisterDuplicateEmail -> 3
        is UserRegisterDuplicateName -> 4
        else -> 5
    }
    call.respondRedirect("/register?a=$specialMessageIndex")
}
