package dev.lunarcoffee.blazelight

import dev.lunarcoffee.blazelight.model.api.users.UserLoginResult
import dev.lunarcoffee.blazelight.model.api.users.UserRegistrar
import dev.lunarcoffee.blazelight.routes.*
import dev.lunarcoffee.blazelight.routes.sessions.UserSession
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.KtorExperimentalAPI
import org.slf4j.event.Level
import java.io.File

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
    install(Authentication) {
        form("loginAuth") {
            userParamName = "username"
            passwordParamName = "password"

            validate {
                if (UserRegistrar.tryLogin(it.name, it.password) == UserLoginResult.SUCCESS)
                    UserIdPrincipal(it.name)
                else
                    null
            }
            skipWhen { it.sessions.get<UserSession>() != null }
        }
    }

    install(CachingHeaders) {
        // Cache images for a day.
        options { outgoingContent ->
            if (outgoingContent.contentType?.withoutParameters() == ContentType.Image.Any)
                CachingOptions(CacheControl.MaxAge(86_400), null)
            else
                null
        }
    }

    install(CallLogging) { level = Level.INFO }
    install(CORS)

    install(Sessions) {
        cookie<UserSession>(
            "BlazelightUserSession",
            directorySessionStorage(File(".sessions"), true)
        )
    }

    routing {
        static {
            staticRootFolder = File("resources/static")
            file("favicon.ico", "img/favicon.ico")

            for (path in listOf("css", "js", "img"))
                static(path) { files(path) }
        }

        homeRoute()
        registerRoute()
        registerPostRoute()
        loginRoute()
    }
}
