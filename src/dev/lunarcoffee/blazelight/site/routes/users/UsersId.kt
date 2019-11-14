package dev.lunarcoffee.blazelight.site.routes.users

import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.*
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Routing.usersIdRoute() = get("/users/{id}") {
    val viewingUser = call.sessions.get<UserSession>()?.getUser()
    val user = call.parameters["id"]?.toLongOrNull()?.getUser()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate(user.username, call, s)) {
        content {
            breadcrumbs {
                crumb("/users", s.users)
                thisCrumb(call, user.username)
            }
            br()

            h3(classes = "title") { b { +user.username } }
            hr()
            padding(6)
            p {
                b { +s.joined }
                +": ${user.creationTime.toTimeDay(user)}"
                br()
                b { +s.posts }
                +": ${user.commentIds.size}"
                br()
                b { +s.realName }
                +": ${user.realName ?: s.noneParen}"
                br()
                b { +s.description }
                +": ${user.description ?: s.noneParen}"
            }

            padding(4)
            hr()
            padding(4)
            p {
                b { +s.timeZone }
                +": ${user.settings.zoneId.id}"
                br()
                b { +s.languageWord }
                +": ${user.settings.language.languageName}"
            }

            padding(4)
            hr()
            padding(4)
            p {
                b { +s.threads }
                +": ${user.threadIds.size}"
                br()
                b { +s.recentThreads }
                +": "

                if (user.threadIds.isEmpty()) {
                    +"(none)"
                } else {
                    br()
                    padding(6)

                    val orderedThreads = user
                        .threadIds
                        .takeLast(8)
                        .map { it.getThread()!! }
                        .sortedByDescending { it.creationTime }

                    for (thread in orderedThreads) {
                        a(href = "/forums/view/${thread.forumId}/${thread.id}", classes = "a2") {
                            +thread.title.textOrEllipsis(70)
                            +" (${thread.commentIds.size})"
                        }
                        i(classes = "forum-topic ltab") {
                            +thread.creationTime.toTimeDisplay(viewingUser)
                        }
                        br()
                    }
                }
            }

            // Show a settings button if the viewer is viewing their own profile page.
            if (user.id == call.sessions.get<UserSession>()?.getUser()?.id) {
                padding(6)
                hr()
                padding(14)
                a(href = "/users/${user.id}/settings", classes = "button-1") { +s.settings }
                padding(8)
            }
        }
    }
}
