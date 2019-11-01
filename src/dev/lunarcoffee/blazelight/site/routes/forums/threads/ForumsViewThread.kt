package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.site.std.*
import dev.lunarcoffee.blazelight.site.std.bbcode.BBCodeLexer
import dev.lunarcoffee.blazelight.site.std.bbcode.BBCodeParser
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

            h3 {
                b { +"Thread" }
                plusButton("/forums/view/${forum.id}/${thread.id}/add", "New Post")
            }
            pageNumbers(page, pageCount, call)
            hr()
            padding(4)

            val user = call.sessions.get<UserSession>()?.getUser()
            for ((index, commentId) in commentPage.withIndex()) {
                val comment = commentId.getComment()!!
                val author = comment.authorId.getUser()!!

                div(classes = "forum-list-item comment-list-item") {
                    div(classes = "commenter-info") {
                        a(href = "/users/${author.id}", classes = "a1 commenter-username") {
                            b(classes = "title commenter-username") { +author.username }
                        }
                        p(classes = "rem-p8") {
                            +"Posts: ${author.commentIds.size}"
                            br()
                            +"Joined: ${author.creationTime.toTimeDay(user)}"
                        }
                    }
                    div(classes = "comment") {
                        div(classes = "ctmr") {
                            // Show title on first comment.
                            if (thread.firstPost?.id == comment.id) {
                                b { +thread.title }
                                padding(6)
                            }
                            p(classes = "comment-text") { renderWithBBCode(comment.contentRaw) }
                            padding(5)
                        }
                        div {
                            i(classes = "thread-l forum-topic") {
                                +comment.creationTime.toTimeDisplay(user)
                                span(classes = "float-r") { +"#${index + 1}" }
                            }
                            br()
                            hr(classes = "hr-dot cdv")
                        }
                    }
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
