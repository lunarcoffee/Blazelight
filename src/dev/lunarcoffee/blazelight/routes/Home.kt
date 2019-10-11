package dev.lunarcoffee.blazelight.routes

import dev.lunarcoffee.blazelight.routes.sessions.UserSession
import dev.lunarcoffee.blazelight.routes.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Routing.homeRoute() = get("/") {
    call.respondHtmlTemplate(HeaderBarTemplate("Home")) {
        content {
            p { +"blazelight" }
        }
        personalized {
            if (call.sessions.get<UserSession>() == null) {
                a(href = "/register", classes = "header-top-button") { +"Register" }
                a(href = "/login", classes = "header-top-button") { +"Login" }
            } else {
                a(href = "/me", classes = "header-top-button") { +"My Profile" }
                a(href = "/logout", classes = "header-top-button") { +"Log Out" }
            }
        }
    }
}
