package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.routes.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.loginRoute() = get("/login") {
    val failed = call.parameters["a"]

    call.respondHtmlTemplate(HeaderBarTemplate("Login", call)) {
        content {
            // This message will be displayed upon failure to login.
            if (failed == "0")
                p { +"Invalid username or password!" }

            p { +"Login to your account:" }
            form(action = "/login", method = FormMethod.post) {
                input(type = InputType.text, name = "username", classes = "fi-text fi-top") {
                    placeholder = "Username"
                }
                br()
                input(type = InputType.password, name = "password", classes = "fi-text") {
                    placeholder = "Password"
                }
                br()
                input(type = InputType.submit, classes = "button-1") { value = "Login" }
            }
        }
    }
}
