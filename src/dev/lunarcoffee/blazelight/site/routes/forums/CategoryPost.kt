package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.forums.CategoryAddResult
import dev.lunarcoffee.blazelight.model.api.forums.CategoryManager
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.categoryPostRoute() = post("/forums/category") {
    val name = call.receiveParameters()["name"]!!
    val user = call.sessions.get<UserSession>()!!.getUser()

    val index = when (CategoryManager.add(name, user)) {
        CategoryAddResult.INSUFFICIENT_PERMISSIONS -> 0
        CategoryAddResult.INVALID_NAME -> 1
        else -> 2
    }
    call.respondRedirect("/forums?a=$index")
}
