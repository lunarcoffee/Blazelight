package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.shared.language.prep
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.formattedTextInput
import dev.lunarcoffee.blazelight.site.std.textOrEllipsis
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.forumsViewThreadAdd() = get("/forums/view/{forumId}/{threadId}/add") {
    val params = call.parameters

    val messageIndex = params["a"]?.toIntOrNull()
    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    val thread = params["threadId"]?.toLongOrNull()?.getThread()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${s.thread} - ${thread.id} - ${s.addPost}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                crumb("/forums/view/${forum.id}/${thread.id}", thread.title.textOrEllipsis(60))
                thisCrumb(call, s.addPost)
            }
            br()

            h3 {
                +s.newPostHeading
                b { +s.threadIdFormat.prep(thread.id) }
                +":"
            }
            hr()
            form(action = call.request.path(), method = FormMethod.post) {
                formattedTextInput(s)
                hr()
                input(type = InputType.submit, classes = "button-1") { value = s.post }

                // This message will be displayed upon a special thread creation event.
                if (messageIndex == 0)
                    span(classes = "red") { +s.invalidContent1To10000 }
            }

            // TODO: Attachments, emotes, thread icon.
        }
    }
}
