package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.site.std.bbcode.formattedTextInput
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
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

private val msg = "That content is invalid! It must be 30 to 10000 characters long (inclusive)!"

fun Route.forumsViewThreadAdd() = get("/forums/view/{forumId}/{threadId}/add") {
    val params = call.parameters

    val messageIndex = params["a"]?.toIntOrNull()
    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    val thread = params["threadId"]?.toLongOrNull()?.getThread()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate("Thread - ${thread.id} - Add Post", call)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", "Forums")
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                crumb("/forums/view/${forum.id}/${thread.id}", thread.title.textOrEllipsis(60))
                thisCrumb(call, "Add Post")
            }
            br()

            h3 {
                +"Post to thread "
                b { +"#${thread.id}" }
                +":"
            }
            hr()
            form(action = call.request.path(), method = FormMethod.post) {
                formattedTextInput()
                hr()
                input(type = InputType.submit, classes = "button-1") { value = "Post" }

                // This message will be displayed upon a special thread creation event.
                if (messageIndex == 0)
                    span(classes = "red") { +msg }
            }

            // TODO: Attachments, emotes, thread icon.
        }
    }
}
