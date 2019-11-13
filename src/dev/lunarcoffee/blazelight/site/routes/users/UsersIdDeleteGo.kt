package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.api.users.registrar.UserDeleteManager
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.*

fun Route.usersIdDeleteGoRoute() = get("/users/{id}/delete/go") {
    val user = call.parameters["id"]?.toLongOrNull()?.getUser()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    // Prevent deletion of user accounts by other users.
    if (user.id != call.sessions.get<UserSession>()!!.getUser()?.id)
        return@get call.respond(HttpStatusCode.Forbidden)

    // Delete the user and their session.
    UserDeleteManager.delete(user)
    call.sessions.clear<UserSession>()
    call.respondRedirect("/")
}
