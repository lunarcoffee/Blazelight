package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
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
    "You don't have enough permissions to do that!",
    "That name is invalid! It must be 1 to 100 characters long (inclusive)!",
    "That topic is invalid! It must be 1 to 300 characters long (inclusive)!"
)

fun Route.forumsAddRoute() = get("/forums/add") {
    val params = call.parameters

    val messageIndex = params["a"]?.toIntOrNull()
    val category = params["b"]?.toLongOrNull()?.getCategory()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate("Add Forum", call)) {
        content {
            breadcrumbs {
                val name = category.name
                crumb("/forums", "Forums")
                crumb("/forums/$name#$name", name)
                crumb(call.request.uri, "Create Forum")
            }
            br()

            h3 {
                +"Create a new forum in "
                b { +category.name }
                +":"
            }
            hr()
            form(action = "/forums/add", method = FormMethod.post) {
                // Hidden category ID.
                input(type = InputType.hidden, name = "category") {
                    value = category.id.toString()
                }

                input(type = InputType.text, name = "name", classes = "fi-text fi-top") {
                    placeholder = "Name"
                }
                br()
                input(type = InputType.text, name = "topic", classes = "fi-text") {
                    placeholder = "Topic"
                }
                hr()
                input(type = InputType.submit, classes = "button-1") { value = "Create" }

                // This message will be displayed upon a special forum creation event.
                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }
        }
    }
}
