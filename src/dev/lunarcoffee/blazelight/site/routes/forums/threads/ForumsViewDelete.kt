package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.prep
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.padding
import dev.lunarcoffee.blazelight.site.std.path
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.forumsViewDelete() = get("/forums/view/{forumId}/delete") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val params = call.parameters

    // Ensure that the deleter is an administrator; only they can manage forums.
    if (!user.isAdmin)
        return@get call.respond(HttpStatusCode.Forbidden)

    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${s.forums} - ${forum.name} - ${s.deleteForum}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                thisCrumb(call, s.deleteForum)
            }
            br()

            h3 {
                +if (selfDeleting) s.deleteThreadTitle else s.forceDeleteThreadTitle
                b { +"#${thread.id}" }
                +":"
            }
            hr()
            p {
                // "Your thread" or "OtherPerson's thread."
                val threadNoun = if (selfDeleting)
                    s.your
                else
                    "${thread.authorId.getUser()!!.username}${s.apoS}"

                +s.deleteThreadConfirmMessage.prep(threadNoun)
                b { +s.deleteThreadConfirmBold }
            }

            padding(20)
            a(href = "${call.path}/go", classes = "button-1") { +s.delete }
            a(href = "/forums/view/${forum.id}/${thread.id}", classes = "button-1") { +s.cancel }
            padding(8)
        }
    }
}
