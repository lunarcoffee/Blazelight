package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.comments.*
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

fun Route.forumsViewThreadCommentEditPost() {
    post("/forums/view/{forumId}/{threadId}/{commentId}/edit") {
        val params = call.parameters

        val user = call.sessions.get<UserSession>()!!.getUser()!!
        val commentId = params["commentId"]!!.toLong()

        // Ensure only the author can edit their comments.
        if (commentId !in user.commentIds)
            return@post call.respond(HttpStatusCode.Forbidden)

        val content = call.receiveParameters()["content"]!!
        val forumId = params["forumId"]!!.toLong()
        val threadId = params["threadId"]!!.toLong()

        val comment = commentId.getComment()!!.apply { contentRaw = content }
        call.respondRedirect(
            if (CommentEditManager.edit(comment) == CommentAddResult.INVALID_CONTENT)
                "/forums/view/$forumId/$threadId/edit?a=0"
            else
                "/forums/view/$forumId/$threadId"
        )
    }
}
