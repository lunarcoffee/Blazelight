package dev.lunarcoffee.blazelight.site.routes.users

import io.ktor.application.call
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.get

fun Routing.usersRoute() = get("/users") { call.respondRedirect("/") }
