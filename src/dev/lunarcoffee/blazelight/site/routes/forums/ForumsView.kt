package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.*
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Routing.forumsViewRoute() = get("/forums/view/{id}") {
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate("Forums - ${forum.name}", call)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", "Forums")
                crumb("/forums/${category.name}#${category.name}", category.name)
                thisCrumb(call, forum.name)
            }
            br()

            h3 { b { +forum.name } }
            hr()
            if (forum.threadIds.isEmpty()) {
                p { +"There are no threads in this forum." }
                padding(8)
            } else {
                // [user] is used for local time display.
                val user = call.sessions.get<UserSession>()?.getUser()

                for (threadId in forum.threadIds) {
                    val thread = threadId.getThread()!!
                    div(classes = "forum-list-item") {
                        a(href = "/forums/view/${forum.id}/${thread.id}", classes = "a1") {
                            +thread.title.textOrEllipsis(100)
                            +" (${thread.commentIds.size})"
                        }
                        p(classes = "forum-topic") {
                            +thread.firstPost!!.contentRaw.textOrEllipsis(200)
                        }
                        padding(5)
                        i(classes = "thread-l forum-topic") {
                            // Thread author's initial post and timestamp
                            +"Thread started by "
                            val creatorName = thread.firstPost!!.authorId.getUser()!!.username
                            a(href = "/users/$creatorName", classes = "a2") { +creatorName }
                            +" on ${thread.creationTime.toTimeDisplay(user)}"
                            br()

                            // Last post author and timestamp.
                            +"Last post by "
                            val authorName = thread.lastPost!!.authorId.getUser()!!.username
                            a(href = "/users/$authorName", classes = "a2") { +authorName }
                            +" on ${thread.creationTime.toTimeDisplay(user)}"
                        }
                        hr(classes = "hr-dot")
                    }
                }
            }

            div(classes = "ba-wrap") {
                a(href = "/forums/view/${forum.id}/add", classes = "button-1") { +"New Thread" }
            }
        }
    }
}
