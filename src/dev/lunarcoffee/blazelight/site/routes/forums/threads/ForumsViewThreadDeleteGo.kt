package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.threads.ThreadDeleteManager
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.forumsViewThreadDeleteGo() = get("/forums/view/{forumId}/{threadId}/delete/go") {
    val params = call.parameters

    // Ensure that the deleter is the thread's creator, or is an administrator.
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val thread = params["threadId"]?.toLongOrNull()?.getThread()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    if (user.id != thread.authorId && !user.isAdmin)
        return@get call.respond(HttpStatusCode.Forbidden)

    val forumId = params["forumId"]?.toLongOrNull()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    ThreadDeleteManager.delete(thread.id, forumId)
    call.respondRedirect("/forums/view/$forumId")
}
