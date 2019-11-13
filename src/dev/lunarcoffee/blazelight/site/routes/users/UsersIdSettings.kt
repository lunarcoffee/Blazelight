package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
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

fun Route.usersIdSettingsRoute() = get("/users/{id}/settings") {
    val user = call.parameters["id"]?.toLongOrNull()?.getUser()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    // Prevent modification of other users' settings.
    if (user.id != call.sessions.get<UserSession>()!!.getUser()?.id)
        return@get call.respond(HttpStatusCode.Forbidden)

    call.respondHtmlTemplate(HeaderBarTemplate(user.username, call, s)) {
        content {
            breadcrumbs {
                crumb("/users", s.users)
                crumb("/users/${user.id}", user.username)
                thisCrumb(call, s.settings)
            }
            br()

            h3 { b { +"Select theme:" } }
            padding(4)
            form(action = "${call.path}/set", method = FormMethod.post) {
                select(classes = "fi-select") {
                    name = "theme"
                    for (styleName in BL_CONFIG.styles.keys) {
                        option {
                            value = styleName
                            +styleName
                        }
                    }
                }
                br()
                input(type = InputType.submit, classes = "button-1") { value = s.save }
            }
            hr()

            p { +user.username }
            p { +(user.realName ?: s.unsetParen) }
            p { +(user.description ?: s.unsetParen) }
            p { +(user.settings.zoneId.id ?: s.unsetParen) }
            p { +(user.settings.language.name) }
            p { +(user.settings.theme) }

            padding(16)
            a(href = "/users/${user.id}/settings", classes = "button-1") { +s.save }
            a(href = "/users/${user.id}", classes = "button-1") { +s.discard }
            a(href = "/users/${user.id}/delete", classes = "button-1") { +s.deleteAccount }
            padding(8)
        }
    }
}
