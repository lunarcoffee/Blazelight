package dev.lunarcoffee.blazelight

import dev.lunarcoffee.blazelight.site.routes.*
import dev.lunarcoffee.blazelight.site.routes.forums.categoryPostRoute
import dev.lunarcoffee.blazelight.site.routes.forums.forumsRoute
import dev.lunarcoffee.blazelight.site.sessions.UserSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respondRedirect
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.KtorExperimentalAPI
import org.slf4j.event.Level
import java.io.File

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
    configAuth()
    configCache()
    configSessions()
    configRouting()

    install(CallLogging) { level = Level.INFO }
    install(CORS)
}

fun Application.configAuth() {
    install(Authentication) {
        form("loginAuth") {
            skipWhen { it.sessions.get<UserSession>() != null }
            challenge { call.respondRedirect("/login") }
        }
    }
}

fun Application.configCache() {
    install(CachingHeaders) {
        // Cache images for a day.
        options { outgoingContent ->
            if (outgoingContent.contentType?.withoutParameters() == ContentType.Image.Any)
                CachingOptions(CacheControl.MaxAge(86_400), null)
            else
                null
        }
    }
}

@KtorExperimentalAPI
fun Application.configSessions() {
    install(Sessions) {
        cookie<UserSession>("BlazelightSession", directorySessionStorage(File(".sessions"), true))
    }
}

fun Application.configRouting() {
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
        loginPostRoute()
        forumsRoute()

        authenticate("loginAuth") {
            profileRoute()
            logoutRoute()
            categoryPostRoute()
        }
    }
}
