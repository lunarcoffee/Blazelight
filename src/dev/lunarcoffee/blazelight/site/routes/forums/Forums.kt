package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryManager
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.runBlocking
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
            for (category in categories) {
                div(classes = "category") {
                    h3 {
                        +category.name
                        a(href = "/forums/add", classes = "b-img-a") {
                            img(alt = "Add Forum", src = "/img/green-plus.png", classes = "b-plus")
                        }
                    }
                    hr()

                    if (category.forumIds.isEmpty()) {
                        p { +"There are no forums in this category." }
                    } else {
                        for (id in category.forumIds) {
                            val forum = runBlocking { id.getForum() }
                            div(classes = "forum-list-item") {

                            }
                        }
                    }
                }
            }

            if (user?.isAdmin == true) {
                hr()
                h3 { +"Create a new forum category:" }
                form(action = "/forums/category", method = FormMethod.post, classes = "f-inline") {
                    input(type = InputType.text, name = "name", classes = "fi-text fi-top") {
                        placeholder = "Forum name"
                    }
                    input(type = InputType.submit, classes = "button-1 b-inline") {
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
