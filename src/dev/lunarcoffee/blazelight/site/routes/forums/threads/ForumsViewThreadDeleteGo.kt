package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.comments.*
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

fun Route.forumsViewThreadDeleteGo() {
    get("/forums/view/{forumId}/{threadId}/{commentId}/delete/go") {
        val params = call.parameters

        // Ensure that the deleter is the comment's author, or is an administrator.
        val user = call.sessions.get<UserSession>()!!.getUser()!!
        val comment = params["commentId"]?.toLongOrNull()?.getComment()
            ?: return@get call.respond(HttpStatusCode.NotFound)
        if (user.id != comment.authorId && !user.isAdmin)
            return@get call.respond(HttpStatusCode.Forbidden)

        val forumId = params["forumId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)
        val threadId = params["threadId"]?.toLongOrNull()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        CommentDeleteManager.delete(comment, threadId)
        call.respondRedirect("/forums/view/$forumId/$threadId/")
    }
}
