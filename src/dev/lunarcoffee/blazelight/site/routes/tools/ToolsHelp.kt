package dev.lunarcoffee.blazelight.site.routes.tools

import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

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
            a(href = "/tools/help/bbcode", classes = "a2") { +s.helpBBCodeDescription }
        }
    }
}
