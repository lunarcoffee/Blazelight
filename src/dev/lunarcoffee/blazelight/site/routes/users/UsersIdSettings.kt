package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.Language
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

            h3(classes = "title") { b { +"Modify settings:" } }
            hr()
            padding(4)
            form(action = "${call.path}/set", method = FormMethod.post) {
                input(type = InputType.text, name = "realName", classes = "fi-text") {
                    placeholder = s.realName
                    value = user.realName ?: ""
                }
                br()
                input(type = InputType.text, name = "description", classes = "fi-text") {
                    placeholder = s.description
                    value = user.description ?: ""
                }
                br()
                hr()

                select(classes = "fi-select") {
                    name = "timeZone"
                    for ((index, zoneId) in TimeZoneManager.timeZones.withIndex()) {
                        option {
                            if (zoneId == user.settings.zoneId)
                                selected = true
                            value = index.toString()
                            +zoneId.id
                        }
                    }
                }
                +s.timeZoneParen

                br()
                select(classes = "fi-select") {
                    name = "language"
                    for ((index, lang) in Language.values().withIndex()) {
                        option {
                            if (lang.name == user.settings.language.name)
                                selected = true
                            value = index.toString()
                            +lang.languageName
                        }
                    }
                }
                +s.languageParen

                br()
                select(classes = "fi-select") {
                    name = "theme"
                    for (themeName in BL_CONFIG.styles.keys) {
                        option {
                            if (user.settings.theme == themeName)
                                selected = true
                            value = themeName
                            +themeName
                        }
                    }
                }
                +s.themeParen
                br()
                hr()

                input(type = InputType.submit, classes = "button-1") { value = s.save }
                a(href = "/users/${user.id}", classes = "button-1") { +s.discard }
                a(href = "/users/${user.id}/delete", classes = "button-1") { +s.deleteAccount }
            }
        }
    }
}
