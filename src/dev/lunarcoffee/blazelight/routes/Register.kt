package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.routes.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.registerRoute() = get("/register") {
    call.respondHtmlTemplate(HeaderBarTemplate("Register")) {
        content {
            p { +"Register for an account:" }
            form(action = "/register", method = FormMethod.post) {
                input(type = InputType.text, name = "email") { required = true }
                br()
                input(type = InputType.text, name = "username") { required = true }
                br()
                input(type = InputType.password, name = "password") { required = true }
                br()
                input(type = InputType.submit)
            }
        }
    }
}
