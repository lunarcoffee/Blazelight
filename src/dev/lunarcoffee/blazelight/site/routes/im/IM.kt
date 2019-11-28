package dev.lunarcoffee.blazelight.site.routes.im

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.api.imdata.getIMDataList
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.std.textOrEllipsis
import dev.lunarcoffee.blazelight.site.std.toTimeDisplay
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.imRoute() = get("/im") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val imDataList = user.imDataListId.getIMDataList()!!

    call.respondHtmlTemplate(HeaderBarTemplate(s.im, call, s)) {
        content {
            breadcrumbs { thisCrumb(call, s.imCap) }
            br()

            h3(classes = "title") { b { +s.imCap } }
            hr()

            for (data in imDataList.data) {
                div(classes = "forum-list-item") {
                    a(href = "/forums/v", classes = "a1 title") {
                        +"".textOrEllipsis(70)
                    }
                    p(classes = "forum-topic title") {
                        // TODO: last sent message
                        +data.recipientId.getUser()!!.username
                    }
                    i(classes = "thread-l forum-topic title") {
                        // TODO: last sent message timestamp
                        // Last post author and timestamp.
                        val last = data.messages.last()
                        +" ${s.timeOn} ${last.creationTime.toTimeDisplay(user)}"
                    }
                    hr(classes = "hr-dot")
                }
            }
        }
    }
}
