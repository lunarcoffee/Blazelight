package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.loginRoute() = get("/login") {
    val failed = call.parameters["a"]

    call.respondHtmlTemplate(HeaderBarTemplate("Login", call)) {
        content {
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

                // This message will be displayed upon failure to login.
                if (failed == "0")
                    span(classes = "red") { +"Invalid username or password!" }
            }
        }
    }
}
