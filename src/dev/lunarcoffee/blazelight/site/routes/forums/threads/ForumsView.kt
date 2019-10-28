package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
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
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Routing.forumsViewRoute() = get("/forums/view/{id}") {
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val page = call.parameters["p"]?.toIntOrNull() ?: 0
    val pageCount = forum.threadIds.size / BL_CONFIG.pageSize
    if (page > pageCount)
        return@get call.respond(HttpStatusCode.NotFound)

    val threadPage = forum.threadIds.drop(page * BL_CONFIG.pageSize).take(BL_CONFIG.pageSize)

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
            div(classes = "page-numbers") {
                if (pageCount < 10) {
                    for (index in 0 until pageCount) {
                        a(href = "${call.request.path()}?p=$index", classes = "a2 a-page") {
                            +if (index == page) "(${(index + 1)})" else (index + 1).toString()
                        }
                    }
                } else {
                    if (page > 0) {
                        // First and previous page buttons.
                        a(href = call.request.path(), classes = "a2 a-page") { +"First" }
                        a(href = "${call.request.path()}?p=${page - 1}", classes = "a2 a-page") {
                            +"Prev"
                        }
                    }
                    // Current page button/indicator.
                    a(href = "${call.request.path()}?p=$page", classes = "a2 a-page") {
                        +"(${page + 1})"
                    }
                    if (page < pageCount - 1) {
                        // Next and last page buttons.
                        a(href = "${call.request.path()}?p=${page + 1}", classes = "a2 a-page") {
                            +"Next"
                        }
                        val last = pageCount - 1
                        a(href = "${call.request.path()}?p=$last", classes = "a2 a-page") {
                            +"Last"
                        }
                    }
                    form(action = call.request.path(), classes = "f-page") {
                        input(type = InputType.text, name = "p", classes = "fi-text fi-top")
                        input(type = InputType.submit, classes = "button-1 b-inline") {
                            style = "display: none;"
                        }
                    }
                }
            }

            hr()
            if (forum.threadIds.isEmpty()) {
                p { +"There are no threads in this forum." }
                padding(8)
            } else {
                // [user] is used for local time display.
                val user = call.sessions.get<UserSession>()?.getUser()

                for (threadId in threadPage) {
                    val thread = threadId.getThread()!!
                    div(classes = "forum-list-item") {
                        a(href = "/forums/view/${forum.id}/${thread.id}", classes = "a1") {
                            +thread.title.textOrEllipsis(70)
                            +" (${thread.commentIds.size})"
                        }
                        p(classes = "forum-topic") {
                            +thread.firstPost!!.contentRaw.textOrEllipsis(120)
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

            padding(12)
            a(href = "/forums/view/${forum.id}/add", classes = "button-1") { +"New Thread" }
            padding(8)
        }
    }
}
