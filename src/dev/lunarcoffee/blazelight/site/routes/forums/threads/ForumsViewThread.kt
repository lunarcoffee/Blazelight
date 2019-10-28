package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
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
import kotlin.math.ceil

fun Routing.forumsViewThread() = get("/forums/view/{forumId}/{threadId}") {
    val params = call.parameters
    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    val thread = params["threadId"]?.toLongOrNull()?.getThread()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val page = call.parameters["p"]?.toIntOrNull() ?: 0
    val pageCount = ceil(thread.commentIds.size.toDouble() / BL_CONFIG.commentPageSize).toInt()
    if (page !in 0 until pageCount)
        return@get call.respond(HttpStatusCode.NotFound)

    val commentPage = thread
        .commentIds
        .drop(page * BL_CONFIG.commentPageSize)
        .take(BL_CONFIG.commentPageSize)

    call.respondHtmlTemplate(HeaderBarTemplate("Thread - ${thread.id}", call)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", "Forums")
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                thisCrumb(call, thread.title.textOrEllipsis(60))
            }
            br()

            h3 { b { +"Thread" } }
            pageNumbers(page, pageCount, call)
            hr()

            val user = call.sessions.get<UserSession>()?.getUser()
            for (commentId in commentPage) {
                val comment = commentId.getComment()!!

                div(classes = "forum-list-item") {
                    // Show title on first comment.
                    if (thread.firstPost?.id == comment.id) {
                        b { +thread.title }
                        padding(6)
                    }

                    p(classes = "comment-content") { +comment.contentRaw } // TODO: BBCode
                    padding(5)
                    i(classes = "thread-l forum-topic") {
                        // Thread author's initial post and timestamp
                        +"Posted by "
                        val creatorName = comment.authorId.getUser()!!.username
                        a(href = "/users/$creatorName", classes = "a2") { +creatorName }
                        +" on ${comment.creationTime.toTimeDisplay(user)}"
                        br()
                    }
                    hr(classes = "hr-dot")
                }
            }

            padding(12)
            a(href = "/forums/view/${forum.id}/${thread.id}/add", classes = "button-1") {
                +"New Post"
            }
            padding(8)
        }
    }
}
