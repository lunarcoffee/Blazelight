package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.s
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

fun Routing.forumsView() = get("/forums/view/{id}") {
    val user = call.sessions.get<UserSession>()?.getUser()
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val page = call.parameters["p"]?.toIntOrNull() ?: 0
    val pageCount = ceil(forum.threadIds.size.toDouble() / BL_CONFIG.threadPageSize).toInt()
    if (page !in 0 until pageCount && forum.threadIds.isNotEmpty())
        return@get call.respond(HttpStatusCode.NotFound)

    val threadPage = forum
        .threadIds
        .drop(page * BL_CONFIG.threadPageSize)
        .take(BL_CONFIG.threadPageSize)

    call.respondHtmlTemplate(HeaderBarTemplate("${s.forums} - ${forum.name}", call, s)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                thisCrumb(call, forum.name)
            }
            br()

            h3(classes = "title") {
                b { +forum.name }
                plusButton("/forums/view/${forum.id}/add", s.newThread)

                if (user?.isAdmin == true)
                    deleteButton("/forums/view/${forum.id}/delete", s.deleteForum)
            }
            pageNumbers(page, pageCount, call, s)
            hr()

            if (forum.threadIds.isEmpty()) {
                p { +s.noThreadsInForum }
                padding(8)
            } else {
                for (threadId in threadPage) {
                    val thread = threadId.getThread()!!

                    div(classes = "forum-list-item") {
                        a(href = "/forums/view/${forum.id}/${thread.id}", classes = "a1 title") {
                            +thread.title.textOrEllipsis(70)
                            +" (${thread.commentIds.size})"
                        }
                        p(classes = "forum-topic title") {
                            +thread.firstPost!!.contentRaw.textOrEllipsis(120)
                        }
                        padding(5)
                        i(classes = "thread-l forum-topic title") {
                            // Thread author's initial post and timestamp
                            +s.startedBy
                            val author = thread.firstPost!!.authorId.getUser()!!
                            a(href = "/users/${author.id}", classes = "a2") { +author.username }
                            +" ${s.timeOn} ${thread.creationTime.toTimeDisplay(user)}"
                            br()

                            // Last post author and timestamp.
                            +s.lastPostBy
                            val last = thread.lastPost!!.authorId.getUser()!!
                            a(href = "/users/${last.id}", classes = "a2") { +last.username }
                            +" ${s.timeOn} ${thread.lastPost!!.creationTime.toTimeDisplay(user)}"
                        }
                        hr(classes = "hr-dot")
                    }
                }
            }

            padding(if (forum.threadIds.isEmpty()) 12 else 6)
            a(href = "/forums/view/${forum.id}/add", classes = "button-1") { +s.newThread }
            div(classes = "bottom-page-numbers") { pageNumbers(page, pageCount, call, s) }
            padding(8)
        }
    }
}
