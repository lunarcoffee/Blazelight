package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.routes.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

private val specialMessages = listOf(
    "That email is invalid!",
    "That username is invalid! It must be at most 40 in length.",
    "That password is invalid! It must be 8-200 in length.",
    "That email is taken!",
    "That username is taken!",
    "Registration successful! You may now log in."
)

fun Routing.registerRoute() = get("/register") {
    val specialMessageIndex = call.parameters["a"]?.toIntOrNull()

    call.respondHtmlTemplate(HeaderBarTemplate("Register", call)) {
        content {
            // This message will be displayed upon a status after register.
            if (specialMessageIndex in specialMessages.indices)
                p { +specialMessages[specialMessageIndex!!] }

            p { +"Register for an account:" }
            form(action = "/register", method = FormMethod.post) {
                input(type = InputType.text, name = "email", classes = "fi-text fi-top") {
                    placeholder = "Email"
                }
                br()
                input(type = InputType.text, name = "username", classes = "fi-text") {
                    placeholder = "Username"
                }
                br()
                input(type = InputType.password, name = "password", classes = "fi-text") {
                    placeholder = "Password"
                }
                br()
                input(type = InputType.submit, classes = "button-1") { value = "Register" }
            }
        }
    }
}
