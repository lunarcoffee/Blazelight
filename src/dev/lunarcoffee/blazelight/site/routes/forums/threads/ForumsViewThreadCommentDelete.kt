package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.prep
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.*
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.forumsViewThreadCommentDelete() {
    get("/forums/view/{forumId}/{threadId}/{commentId}/delete") {
        val params = call.parameters

        val user = call.sessions.get<UserSession>()!!.getUser()!!
        val comment = params["commentId"]?.toLongOrNull()?.getComment()
            ?: return@get call.respond(HttpStatusCode.NotFound)
        val selfDeleting = user.id == comment.authorId

        // Ensure that the deleter is the comment's author, or is an administrator.
        if (!selfDeleting && !user.isAdmin)
            return@get call.respond(HttpStatusCode.Forbidden)

        val thread = params["threadId"]?.toLongOrNull()?.getThread()
            ?: return@get call.respond(HttpStatusCode.NotFound)
        val forum = params["forumId"]?.toLongOrNull()?.getForum()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        // Redirect to the thread delete page if the user somehow manages to delete the first post
        // of a thread without already being on the thread delete page.
        if (thread.firstPost!!.id == comment.id)
            return@get call.respondRedirect("/forums/view/${forum.id}/${thread.id}/delete")

        val pageTitle = if (selfDeleting) s.deletePostCap else s.forceDeletePostCap
        val template = HeaderBarTemplate("${s.thread} - ${thread.id} - $pageTitle", call, s)
        call.respondHtmlTemplate(template) {
            content {
                breadcrumbs {
                    val category = forum.categoryId.getCategory()!!
                    crumb("/forums", s.forums)
                    crumb("/forums/${category.name}#${category.name}", category.name)
                    crumb("/forums/view/${forum.id}", forum.name)
                    crumb(
                        "/forums/view/${forum.id}/${thread.id}",
                        "Thread: ${thread.title.textOrEllipsis(60)}"
                    )
                    thisCrumb(call, pageTitle)
                }
                br()

                h3 {
                    // "Your post" or "OtherPerson's post."
                    +if (selfDeleting) s.deletePost else s.forceDeletePost
                    b { +"#${comment.id}" }
                    +":"
                }
                hr()
                p {
                    val commentNoun = if (selfDeleting)
                        s.your
                    else
                        "${comment.authorId.getUser()!!.username}${s.apoS}"
                    +s.deleteConfirmMessage.prep(commentNoun)
                }

                padding(20)
                a(href = "${call.path}/go", classes = "button-1") { +s.delete }
                a(href = "/forums/view/${forum.id}/${thread.id}", classes = "button-1") {
                    +s.cancel
                }
                padding(8)
            }
        }
    }
}
