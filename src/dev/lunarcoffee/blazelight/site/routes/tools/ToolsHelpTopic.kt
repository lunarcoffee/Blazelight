package dev.lunarcoffee.blazelight.site.routes.tools

import dev.lunarcoffee.blazelight.shared.language.languageResourceRoot
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.renderWithBBCode
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*
import java.io.File

fun Routing.toolsHelpTopicRoute() = get("/tools/help/{topic}") {
    val topic = call.parameters["topic"]!!
    val topicTextLines = File("$languageResourceRoot/$topic.txt").readLines()
    val topicName = topicTextLines[0].substringAfter("#").trim()
    val topicText = topicTextLines.drop(1).joinToString("\n")

    call.respondHtmlTemplate(HeaderBarTemplate("${s.tools} - ${s.help} - $topicName", call, s)) {
        content {
            breadcrumbs {
                crumb("/tools", s.tools)
                crumb("/tools/help", s.help)
                thisCrumb(call, topicName)
            }
            br()

            h3(classes = "title") { b { +topicName } }
            hr()
            p { renderWithBBCode(topicText, s) }
        }
    }
}
