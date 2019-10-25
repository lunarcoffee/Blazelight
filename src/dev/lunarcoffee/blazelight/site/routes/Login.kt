package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

private val specialMessages = listOf(
    "Invalid username or password!",
    "Please login to continue."
)

fun Routing.loginRoute() = get("/login") {
    val messageIndex = call.parameters["a"]?.toIntOrNull()

    call.respondHtmlTemplate(HeaderBarTemplate("Login", call)) {
        content {
            breadcrumbs { thisCrumb(call, "Login") }
            br()

            h3 { +"Login to your account:" }
            hr()
            form(action = "/login", method = FormMethod.post) {
                input(type = InputType.text, name = "username", classes = "fi-text fi-top") {
                    placeholder = "Username"
                }
                br()
                input(type = InputType.password, name = "password", classes = "fi-text") {
                    placeholder = "Password"
                }
                hr()
                input(type = InputType.submit, classes = "button-1") { value = "Login" }

                // This message will be displayed upon a special login event. The possible events
                // include a permission redirection (user must log in to perform action) and in the
                // case of invalid credentials.
                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }
        }
    }
}
