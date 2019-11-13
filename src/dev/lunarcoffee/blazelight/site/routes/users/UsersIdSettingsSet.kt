package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.api.users.registrar.UserEditManager
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.shared.language.LanguageManager
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.usersIdSettingsSetRoute() = post("/users/{id}/settings/set") {
    val user = call.parameters["id"]?.toLongOrNull()?.getUser() as? DefaultUser
        ?: return@post call.respond(HttpStatusCode.NotFound)

    // Prevent modification of other users' settings.
    if (user.id != call.sessions.get<UserSession>()!!.getUser()?.id)
        return@post call.respond(HttpStatusCode.Forbidden)

    val params = call.receiveParameters()

    val realName = params["realName"]!!
    val description = params["description"]!!

    val timeZone = TimeZoneManager.toTimeZone(params["timeZone"]!!)
    val language = LanguageManager.toLanguage(params["language"]!!.toInt())
    val themeName = params["theme"]!!

    val newUserSettings = user.settings.copy(
        zoneId = timeZone,
        language = language,
        theme = themeName
    )

    val newUser = user.apply {
        this@apply.realName = realName
        this@apply.description = description
        settings = newUserSettings
    }

    // Edit the settings, and send them back to their user page.
    UserEditManager.edit(newUser)
    call.respondRedirect("/users/${user.id}")
}
