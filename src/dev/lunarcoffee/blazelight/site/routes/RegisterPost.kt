package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.model.api.users.registrar.UserRegisterManager
import dev.lunarcoffee.blazelight.model.api.users.registrar.UserRegisterResult
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
        UserRegisterResult.INVALID_EMAIL -> 0
        UserRegisterResult.INVALID_NAME -> 1
        UserRegisterResult.INVALID_PASSWORD -> 2
        UserRegisterResult.DUPLICATE_EMAIL -> 3
        UserRegisterResult.DUPLICATE_USERNAME -> 4
        UserRegisterResult.SUCCESS -> 6
    }
    call.respondRedirect("/register?a=$index")
}
