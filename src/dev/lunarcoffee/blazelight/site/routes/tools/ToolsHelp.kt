package dev.lunarcoffee.blazelight.site.routes.tools

import dev.lunarcoffee.blazelight.shared.language.languageResourceRoot
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.bbcode.BBCodePageReader
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*
import java.io.File

fun Routing.toolsHelp() = get("/tools/help") {
    call.respondHtmlTemplate(HeaderBarTemplate("${s.tools} - ${s.help}", call, s)) {
        content {
            breadcrumbs {
                crumb("/tools", s.tools)
                thisCrumb(call, s.help)
            }
            br()

            h3(classes = "title") { b { +s.help } }
            hr()

            for (file in File("$languageResourceRoot/help").walk()) {
                if (!file.isFile)
                    continue

                a(href = "/tools/help/${file.nameWithoutExtension}", classes = "a2") {
                    val reader = BBCodePageReader(file.path).apply { read() }
                    +reader.topicName
                }
            }
        }
    }
}
