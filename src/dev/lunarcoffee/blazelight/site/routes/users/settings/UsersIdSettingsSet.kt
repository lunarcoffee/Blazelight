package dev.lunarcoffee.blazelight.site.routes.users.settings

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.shared.language.LanguageManager
import dev.lunarcoffee.blazelight.shared.sanitize
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

fun Route.usersIdSettingsSet() = post("/users/{id}/settings/set") {
    val user = call.parameters["id"]?.toLongOrNull()?.getUser() as? DefaultUser
        ?: return@post call.respond(HttpStatusCode.NotFound)

    // Prevent modification of other users' settings.
    if (user.id != call.sessions.get<UserSession>()!!.getUser()?.id)
        return@post call.respond(HttpStatusCode.Forbidden)

    val params = call.receiveParameters()

    val realName = params["realName"]!!.sanitize()
    val description = params["description"]!!.sanitize()

    val timeZone = TimeZoneManager.toTimeZone(params["timeZone"]!!)
    val language = LanguageManager.toLanguage(params["language"]!!.toInt())
    val themeName = params["theme"]!!

    val updateResult = UserSettingsUpdateManager
        .update(user, realName, description, timeZone, language, themeName)
    val index = when (updateResult) {
        UserSettingsUpdateResult.INVALID_REAL_NAME -> 0
        UserSettingsUpdateResult.INVALID_DESCRIPTION -> 1
        UserSettingsUpdateResult.SUCCESS -> return@post call.respondRedirect("/users/${user.id}")
    }
    call.respondRedirect("/users/${user.id}/settings?a=$index")
}
