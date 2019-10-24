package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
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
            breadcrumbs {
                link("/forums", forum.categoryId.getCategory()!!.name)
                link("/forums/view/${forum.id}", forum.name)
            }
            br()

            if (forum.threadIds.isEmpty())
                p { +"There are no threads in this forum." }

            for (threadId in forum.threadIds) {
                val thread = threadId.getThread()!!
                div(classes = "forum-list-item") {
                    a(href = "/forums/view/${forum.id}/${thread.id}", classes = "a1") {
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

            h3 { +"Create a new thread:" }

        }
    }
}
