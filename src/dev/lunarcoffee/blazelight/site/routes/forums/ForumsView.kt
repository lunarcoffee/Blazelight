package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.forumsViewRoute() = get("/forums/view/{id}") {
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate("Forums - ${forum.name}", call)) {
        content {
            for (threadId in forum.threadIds) {
                val thread = threadId.getThread()!!
                div(classes = "forum-list-item") {
                    a(href = "/threads/view/${forum.id}/${thread.id}", classes = "a1") {
                        +thread.title
                        +" (${thread.commentIds.size})"
                    }
                    p(classes = "forum-topic") { +thread.commentIds[0].getComment()!!.contentRaw }
                    a(classes = "forum-topic a1") {
                        +thread.commentIds.last().getComment()!!.authorId.getUser()!!.username
                        +" at {time}"
                    }
                    hr(classes = "hr-dot")
                }
            }
        }
    }
}
