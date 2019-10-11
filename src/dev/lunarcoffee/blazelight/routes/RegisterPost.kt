package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.model.api.users.UserRegistrar
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

    UserRegistrar.tryRegister(email, username, password)
    call.respondRedirect("/register")
}
