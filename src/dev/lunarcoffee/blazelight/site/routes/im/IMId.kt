package dev.lunarcoffee.blazelight.site.routes.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.getIMDataList
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.imId() = get("/im/{dataId}") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val dataId = call.parameters["dataId"]?.toLongOrNull()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    val dataList = user.imDataListId.getIMDataList()!!

    // Verify the IM data is the accessor's.
    val data = dataList.getByDataId(dataId) ?: return@get call.respond(HttpStatusCode.NotFound)

    val recipient = data.recipientId.getUser()!!
    call.respondHtmlTemplate(HeaderBarTemplate("${s.im} - ${recipient.username}", call, s)) {
        content {
            breadcrumbs {
                crumb("/im", s.imCap)
                thisCrumb(call, recipient.username)
            }
            br()

            h3(classes = "title") { b { +recipient.username } }
            hr()

            div(classes = "im-box") { ul(classes = "im-list") }
            hr()
            form(classes = "f-inline im-input") {
                input(type = InputType.text, name = "username", classes = "fi-text fi-top fi-im") {
                    autoComplete = false
                    autoFocus = true
                    placeholder = s.typeSomething
                }
                input(type = InputType.submit, classes = "fi-hidden")
            }
            script(src = "/js/im.js") {}
        }
    }
}
