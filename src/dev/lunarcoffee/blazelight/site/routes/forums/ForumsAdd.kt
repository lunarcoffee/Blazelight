package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.coroutines.runBlocking
import kotlinx.html.*

private val specialMessages = listOf(
    "You don't have enough permissions to do that!",
    "That name is invalid! It must be 1 to 100 characters long (inclusive)!",
    "That topic is invalid! It must be 1 to 300 characters long (inclusive)!"
)

fun Route.forumsAddRoute() = get("/forums/add") {
    val params = call.parameters

    val messageIndex = params["a"]?.toIntOrNull()
    val categoryId = params["b"]?.toLongOrNull() ?: return@get call.respondRedirect("/")

    call.respondHtmlTemplate(HeaderBarTemplate("Add Forum", call)) {
        content {
            h3 {
                +"Create a new forum in "
                b { +runBlocking { categoryId.getCategory().name } }
                +":"
            }
            hr()
            form(action = "/forums/add", method = FormMethod.post) {
                // Hidden category ID.
                input(type = InputType.hidden, name = "category") {
                    value = categoryId.toString()
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
