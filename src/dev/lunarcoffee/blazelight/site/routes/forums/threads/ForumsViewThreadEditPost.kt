package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.threads.*
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.forumsViewThreadEditRoutePost() = post("/forums/view/{forumId}/{threadId}/edit") {
    val params = call.receiveParameters()

    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val thread = call.parameters["threadId"]!!.toLongOrNull()?.getThread()
        ?: return@post call.respond(HttpStatusCode.NotFound)

    // Ensure only the author can edit their thread.
    if (user.id != thread.authorId)
        return@post call.respond(HttpStatusCode.Forbidden)

    val forumId = call.parameters["forumId"]!!.toLong()
    val title = params["title"]!!
    val content = params["content"]!!

    val index = when (ThreadEditManager.edit(thread.id, title, content)) {
        ThreadAddResult.INVALID_NAME -> 0
        ThreadAddResult.INVALID_CONTENT -> 1
        else -> return@post call.respondRedirect("/forums/view/$forumId/${thread.id}")
    }
    call.respondRedirect("/forums/view/$forumId/${thread.id}/edit?a=$index")
}
