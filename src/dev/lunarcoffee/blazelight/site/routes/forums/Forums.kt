package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.forums.CategoryManager
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

private val specialMessages = listOf(
    "You don't have enough permissions to do that!",
    "That name is invalid! It must be 1 to 100 characters long (inclusive)!",
    "Success! Forum created."
)

fun Routing.forumsRoute() = get("/forums") {
    val categories = CategoryManager.getAll()
    val user = call.sessions.get<UserSession>()?.getUser()

    val messageIndex = call.parameters["a"]?.toIntOrNull()

    call.respondHtmlTemplate(HeaderBarTemplate("Forums", call)) {
        content {
            for (category in categories)
                p { +"${category.name} on ${category.creationTime}" }

            if (user?.isAdmin == true) {
                h3 { +"Create a new forum category:" }
                hr()
                form(action = "/forums/category", method = FormMethod.post) {
                    input(type = InputType.text, name = "name", classes = "fi-text fi-top") {
                        placeholder = "Forum name"
                    }
                    hr()
                    input(type = InputType.submit, classes = "button-1") {
                        value = "Create"
                    }

                    // This message will be displayed after an attempt to create a new category.
                    val color = if (messageIndex == specialMessages.lastIndex) "green" else "red"
                    if (messageIndex in specialMessages.indices)
                        span(classes = color) { +specialMessages[messageIndex!!] }
                }
            }
        }
    }
}
