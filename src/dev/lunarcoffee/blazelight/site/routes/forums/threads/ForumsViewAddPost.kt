package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.threads.*
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.forumsViewAddPost() = post("/forums/view/{id}/add") {
    val params = call.receiveParameters()

    val title = params["title"]!!
    val content = params["content"]!!
    val forumId = call.parameters["id"]!!.toLong()
    val user = call.sessions.get<UserSession>()!!.getUser()!!

    val index = when (ThreadAddManager.add(title, content, forumId, user)) {
        ThreadAddResult.INVALID_NAME -> 0
        ThreadAddResult.INVALID_CONTENT -> 1
        else -> return@post call.respondRedirect("/forums/view/$forumId")
    }
    call.respondRedirect("/forums/view/$forumId/add?a=$index")
}
