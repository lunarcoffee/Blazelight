package dev.lunarcoffee.blazelight.site.routes.im

import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.textOrEllipsis
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.imRoute() = get("/im") {
    call.respondHtmlTemplate(HeaderBarTemplate(s.im, call, s)) {
        content {
            breadcrumbs { thisCrumb(call, s.imCap) }
            br()

            h3(classes = "title") { b { +s.imCap } }
            hr()

            div(classes = "forum-list-item") {
                a(href = "/forums/v", classes = "a1 title") {
                    +"".textOrEllipsis(70)
                }
                p(classes = "forum-topic title") {
                    // TODO: last sent message
//                    +thread.firstPost!!.contentRaw.textOrEllipsis(120)
                }
                i(classes = "thread-l forum-topic title") {
                    // TODO: last sent message timestamp
//                    // Last post author and timestamp.
//                    +s.lastPostBy
//                    val last = thread.lastPost!!.authorId.getUser()!!
//                    a(href = "/users/${last.id}", classes = "a2") { +last.username }
//                    +" ${s.timeOn} ${thread.lastPost!!.creationTime.toTimeDisplay(user)}"
                }
                hr(classes = "hr-dot")
            }
        }
    }
}
