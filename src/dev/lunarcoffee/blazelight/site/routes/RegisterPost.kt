package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.model.api.users.registrar.*
import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.shared.language.LanguageManager
import io.ktor.application.call
import io.ktor.request.receiveParameters
import io.ktor.response.respondRedirect
import io.ktor.routing.Routing
import io.ktor.routing.post

fun Routing.registerPostRoute() = post("/register") {
    val params = call.receiveParameters()

    val email = params["email"]!!
    val username = params["username"]!!
    val password = params["password"]!!
    val passwordConfirm = params["password-c"]!!
    val zone = TimeZoneManager.toTimeZone(params["timeZone"]!!)
    val language = LanguageManager.toLanguage(params["language"]!!.toInt())

    if (password != passwordConfirm) {
        call.respondRedirect("/register?a=5")
        return@post
    }

    // This will index into a list of registration status messages in [Routing.registerRoute].
    val index = when (UserRegisterManager.tryRegister(email, username, password, zone, language)) {
        is UserRegisterInvalidEmail -> 0
        is UserRegisterInvalidName -> 1
        is UserRegisterInvalidPassword -> 2
        is UserRegisterDuplicateEmail -> 3
        is UserRegisterDuplicateName -> 4
        else -> 6
    }
    call.respondRedirect("/register?a=$index")
}
