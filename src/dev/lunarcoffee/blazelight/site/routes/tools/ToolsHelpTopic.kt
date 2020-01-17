package dev.lunarcoffee.blazelight.site.routes.tools

import dev.lunarcoffee.blazelight.shared.language.languageResourceRoot
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.bbcode.BBCodePageReader
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.renderWithBBCode
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.toolsHelpTopic() = get("/tools/help/{topic}") {
    val topic = call.parameters["topic"]!!

    val file = BBCodePageReader("$languageResourceRoot/help/$topic.txt")
    if (!file.read())
        return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${s.tools} - ${s.help} - ${file.topicName}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            breadcrumbs {
                crumb("/tools", s.tools)
                crumb("/tools/help", s.help)
                thisCrumb(call, file.topicName)
            }
            br()

            h3(classes = "title") { b { +file.topicName } }
            hr()
            p { renderWithBBCode(file.topicText, s, true) }
        }
    }
}
