package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.clear
import io.ktor.sessions.sessions

fun Route.logoutRoute() = get("/logout") {
    call.sessions.clear<UserSession>()
    call.respondRedirect("/")
}
