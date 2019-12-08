package dev.lunarcoffee.blazelight

import dev.lunarcoffee.blazelight.site.routes.*
import dev.lunarcoffee.blazelight.site.routes.forums.*
import dev.lunarcoffee.blazelight.site.routes.forums.threads.*
import dev.lunarcoffee.blazelight.site.routes.im.imRoute
import dev.lunarcoffee.blazelight.site.routes.im.imStartPost
import dev.lunarcoffee.blazelight.site.routes.statuses.*
import dev.lunarcoffee.blazelight.site.routes.tools.*
import dev.lunarcoffee.blazelight.site.routes.users.*
import dev.lunarcoffee.blazelight.site.routes.users.settings.usersIdSettings
import dev.lunarcoffee.blazelight.site.routes.users.settings.usersIdSettingsSet
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
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

// TODO:
//  - Basic IM functionality.
//  - IM requests.

@KtorExperimentalAPI
@Suppress("unused")
fun Application.module() {
    configAuth()
    configCache()
    configSessions()
    configStatusPages()
    configRouting()

    install(CallLogging) { level = Level.INFO }
    install(CORS)
}

fun Application.configAuth() {
    install(Authentication) {
        form("loginAuth") {
            skipWhen { it.sessions.get<UserSession>() != null }
            challenge { call.respondRedirect("/login?a=1") }
        }
    }
}

fun Application.configCache() {
    val fileTypes = setOf(
        ContentType.Image.PNG,
        ContentType.Image.JPEG,
        ContentType.Image.GIF,
        ContentType.Image.SVG,
        ContentType.Image.XIcon,
        ContentType.Video.MP4,
        ContentType.Video.MPEG,
        ContentType.Video.OGG
    )

    install(CachingHeaders) {
        // Cache certain media types for a day.
        options { outgoingContent ->
            if (outgoingContent.contentType?.withoutParameters() in fileTypes)
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

fun Application.configStatusPages() {
    install(StatusPages) {
        forbiddenStatus()
        notFoundStatus()
        internalServerErrorStatus()
    }
}

fun Application.configRouting() {
    // Route names with less than two words should have "route" appended to their function names.
    routing {
        static {
            staticRootFolder = File("resources/static")
            file("favicon.ico", "img/favicon.ico")

            for (path in listOf("css", "js", "img"))
                static(path) { files(path) }
        }

        authenticate("loginAuth") {
            usersIdSettings()
            usersIdSettingsSet()
            usersIdDelete()
            usersIdDeleteGo()
            logoutRoute()

            categoryPost()
            forumsAdd()
            forumsAddPost()
            forumsViewAdd()
            forumsViewAddPost()
            forumsViewThreadAdd()
            forumsViewThreadAddPost()

            forumsViewThreadEdit()
            forumsViewThreadEditRoutePost()
            forumsViewThreadCommentEdit()
            forumsViewThreadCommentEditPost()

            forumsCategoryDelete()
            forumsCategoryDeleteGo()
            forumsViewDelete()
            forumsViewDeleteGo()
            forumsViewThreadDelete()
            forumsViewThreadDeleteGo()
            forumsViewThreadCommentDelete()
            forumsViewThreadCommentDeleteGo()

            imRoute()
            imStartPost()
        }

        homeRoute()
        registerRoute()
        registerPost()
        loginRoute()
        loginPost()

        usersId()
        usersRoute()

        forumsRoute()
        forumsView()
        forumsViewThread()

        toolsRoute()
        toolsHelp()
        toolsHelpTopic()
    }
}
