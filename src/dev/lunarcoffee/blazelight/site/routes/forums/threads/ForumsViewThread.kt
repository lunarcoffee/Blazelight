package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.comments.getComments
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

fun Routing.forumsViewThread() = get("/forums/view/{forumId}/{threadId}") {
    val user = call.sessions.get<UserSession>()?.getUser()
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
        .getComments()

    call.respondHtmlTemplate(HeaderBarTemplate("${s.thread} - ${thread.id}", call, s)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                thisCrumb(call, "Thread: ${thread.title.textOrEllipsis(60)}")
            }
            br()

            h3 {
                b { +s.thread }
                plusButton("/forums/view/${forum.id}/${thread.id}/add", s.newPost)

                if (user?.isAdmin == true || user?.id == thread.authorId)
                    deleteButton("/forums/view/${forum.id}/${thread.id}/delete", s.deletePostCap)
            }
            pageNumbers(page, pageCount, call, s)
            hr()
            padding(4)

            for ((index, comment) in commentPage.withIndex()) {
                val author = comment.authorId.getUser()!!

                div(classes = "forum-list-item comment-list-item") {
                    div(classes = "commenter-info") {
                        a(href = "/users/${author.id}", classes = "a1 commenter-username") {
                            b(classes = "title commenter-username") { +author.username }
                        }
                        p(classes = "rem-p8") {
                            +"${s.posts}: ${author.commentIds.size}"
                            br()
                            +"${s.joined}: ${author.creationTime.toTimeDay(user)}"
                        }
                    }
                    div(classes = "comment") {
                        div(classes = "ctmr") {
                            // Show title on first comment.
                            if (thread.firstPost?.id == comment.id) {
                                b { +thread.title }
                                padding(6)
                            }
                            p(classes = "comment-text") { renderWithBBCode(comment.contentRaw, s) }
                            padding(5)
                        }
                        div {
                            i(classes = "thread-l forum-topic") {
                                +comment.creationTime.toTimeDisplay(user)
                                span(classes = "float-r post-index") {
                                    val selfAuthor = author.id == user?.id

                                    if (selfAuthor) {
                                        if (index == 0) {
                                            a(href = "${call.path}/edit", classes = "a2 pa") {
                                                +s.editThreadCap
                                            }
                                        } else {
                                            a(
                                                href = "${call.path}/${comment.id}/edit",
                                                classes = "a2 pa"
                                            ) {
                                                +s.edit
                                            }
                                        }
                                    }

                                    if (selfAuthor || user?.isAdmin == true) {
                                        if (index == 0) {
                                            a(href = "${call.path}/delete", classes = "a2 pa") {
                                                +if (selfAuthor)
                                                    s.deleteThread
                                                else
                                                    s.forceDeleteThread
                                            }
                                        } else {
                                            a(
                                                href = "${call.path}/${comment.id}/delete",
                                                classes = "a2 pa"
                                            ) {
                                                +if (selfAuthor) s.delete else s.forceDelete
                                            }
                                        }
                                    }

                                    // Post number of the thread.
                                    val number = page * BL_CONFIG.commentPageSize + index + 1
                                    +"#$number"
                                }
                            }
                            br()
                            hr(classes = "hr-dot cdv")
                        }
                    }
                }
            }

            padding(6)
            a(href = "/forums/view/${forum.id}/${thread.id}/add", classes = "button-1") {
                +s.newPost
            }
            div(classes = "bottom-page-numbers") { pageNumbers(page, pageCount, call, s) }
            padding(8)
        }
    }
}
