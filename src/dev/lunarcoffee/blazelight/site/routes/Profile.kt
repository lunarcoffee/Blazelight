package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.profileRoute() = get("/me") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!

    call.respondHtmlTemplate(HeaderBarTemplate(user.username, call)) {
        content {
            p { +user.username }
            p {
                +(user.realName ?: "(unset)")
                a(href = "google.com") { +"modify" }
            }
            p {
                +(user.description ?: "(unset)")
                a(href = "google.com") { +"modify" }
            }
            p {
                +(user.settings.zoneId.id ?: "(unset)")
                a(href = "google.com") { +"modify" }
            }
            p {
                +(user.settings.language.name)
                a(href = "google.com") { +"modify" }
            }
        }
    }
}
