package dev.lunarcoffee.blazelight.site.routes.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.getIMDataList
import dev.lunarcoffee.blazelight.model.api.users.getUser
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
    val messageIndex = call.parameters["a"]?.toIntOrNull()
    val specialMessages = listOf(s.userNotFound, s.cannotMessageSelf)

    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val imDataList = user.imDataListId.getIMDataList()!!

    call.respondHtmlTemplate(HeaderBarTemplate(s.im, call, s)) {
        content {
            breadcrumbs { thisCrumb(call, s.imCap) }
            br()

            if (imDataList.data.isEmpty())
                p { +s.noConversations }
            else
                h3(classes = "title") { b { +s.imCap } }
            hr()

            for (data in imDataList.data) {
                div(classes = "forum-list-item") {
                    a(href = "/im/${data.id}", classes = "a1 title") {
                        +data.recipientId.getUser()!!.username
                    }
                    p(classes = "forum-topic title") {
                        // TODO: last sent message
                        val last = data.messages.lastOrNull()?.contentRaw ?: s.noMessagesYetParen
                        +last.textOrEllipsis(70)
                    }
                    i(classes = "thread-l forum-topic title") {
                        // Last post author and timestamp.
                        val time = data.messages.lastOrNull()?.creationTime ?: data.creationTime
                        +time.toTimeDisplay(user)
                    }
                    hr(classes = "hr-dot")
                }
            }

            h3 { +s.startConversationHeading }
            form(action = "/im/start", method = FormMethod.post, classes = "f-inline") {
                input(type = InputType.text, name = "username", classes = "fi-text fi-top") {
                    placeholder = s.username
                }
                input(type = InputType.submit, classes = "button-1 b-inline") { value = s.go }

                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }
        }
    }
}
