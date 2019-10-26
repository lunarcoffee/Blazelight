package dev.lunarcoffee.blazelight.site.routes.profile

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.padding
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.profileSettingsRoute() = get("/me/settings") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!

    call.respondHtmlTemplate(HeaderBarTemplate(user.username, call)) {
        content {
            breadcrumbs {
                crumb("/me", "My Profile")
                thisCrumb(call, "Settings")
            }
            br()

            p { +user.username }
            p { +(user.realName ?: "(unset)") }
            p { +(user.description ?: "(unset)") }
            p { +(user.settings.zoneId.id ?: "(unset)") }
            p { +(user.settings.language.name) }
            p { +(user.settings.theme) }

            padding(16)
            a(href = "/me/settings", classes = "button-1") { +"Save" }
            a(href = "/me/settings", classes = "button-1") { +"Discard" }
            padding(8)
        }
    }
}
