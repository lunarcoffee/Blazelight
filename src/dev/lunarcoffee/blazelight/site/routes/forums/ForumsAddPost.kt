package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.forums.ForumAddResult
import dev.lunarcoffee.blazelight.model.api.forums.ForumManager
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.forumsAddPostRoute() = post("/forums/add") {
    val params = call.receiveParameters()

    val categoryId = params["category"]!!.toLong()
    val name = params["name"]!!
    val topic = params["topic"]!!
    val user = call.sessions.get<UserSession>()!!.getUser()!!

    val index = when (ForumManager.add(name, topic, categoryId, user)) {
        ForumAddResult.INSUFFICIENT_PERMISSIONS -> 0
        ForumAddResult.INVALID_NAME -> 1
        ForumAddResult.INVALID_TOPIC -> 2
        else -> return@post call.respondRedirect("/forums")
    }
    call.respondRedirect("/forums/add?a=$index&b=${categoryId}")
}
