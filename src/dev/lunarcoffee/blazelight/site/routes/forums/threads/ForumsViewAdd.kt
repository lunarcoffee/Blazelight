package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.site.std.bbcode.formattedTextInput
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

private val specialMessages = listOf(
    "That title is invalid! It must be 1 to 300 characters long (inclusive)!",
    "That content is invalid! It must be 30 to 10000 characters long (inclusive)!"
)

fun Route.forumsViewAdd() = get("/forums/view/{id}/add") {
    val messageIndex = call.parameters["a"]?.toIntOrNull()
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate("Forums - ${forum.name} - Add Thread", call)) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", "Forums")
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                thisCrumb(call, "Add Thread")
            }
            br()

            h3 {
                +"Create a new thread in "
                b { +forum.name }
                +":"
            }
            hr()
            form(action = call.request.uri, method = FormMethod.post) {
                // Hidden forum ID.
                input(type = InputType.hidden, name = "forum") { value = forum.id.toString() }

                input(type = InputType.text, name = "title", classes = "fi-text fi-top") {
                    placeholder = "Title"
                }
                br()
                formattedTextInput()
                hr()
                input(type = InputType.submit, classes = "button-1") { value = "Create" }

                // This message will be displayed upon a special thread creation event.
                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }

            // TODO: Attachments, emotes, thread icon.
        }
    }
}
