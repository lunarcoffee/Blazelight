package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.padding
import dev.lunarcoffee.blazelight.site.std.path
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.usersIdDeleteRoute() = get("/users/{id}/delete") {
    val user = call.parameters["id"]?.toLongOrNull()?.getUser()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    // Prevent deletion of user accounts by other users.
    if (user.id != call.sessions.get<UserSession>()!!.getUser()?.id)
        return@get call.respond(HttpStatusCode.Forbidden)

    call.respondHtmlTemplate(HeaderBarTemplate("${user.username} - ${s.deleteAccount}", call, s)) {
        content {
            breadcrumbs {
                crumb("/users", s.users)
                crumb("/users/${user.id}", user.username)
                crumb("/users/${user.id}/settings", s.settings)
                thisCrumb(call, s.deleteAccount)
            }
            br()

            h3 { b { +s.deleteAccountTitle } }
            hr()
            p { +s.deleteAccountConfirmMessage }

            padding(20)
            a(href = "${call.path}/go", classes = "button-1") { +s.delete }
            a(href = "/users/${user.id}/settings", classes = "button-1") { +s.cancel }
            padding(8)
        }
    }
}
