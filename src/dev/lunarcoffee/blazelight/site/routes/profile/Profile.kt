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

fun Route.profileRoute() = get("/me") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!

    call.respondHtmlTemplate(HeaderBarTemplate(user.username, call)) {
        content {
            breadcrumbs { thisCrumb(call, "My Profile") }
            br()

            p { +user.username }
            p { +(user.realName ?: "(unset)") }
            p { +(user.description ?: "(unset)") }
            p { +(user.settings.zoneId.id ?: "(unset)") }
            p { +(user.settings.language.name) }
            p { +(user.settings.theme) }

            padding(16)
            a(href = "/me/settings", classes = "button-1") { +"Settings" }
            padding(8)
        }
    }
}
