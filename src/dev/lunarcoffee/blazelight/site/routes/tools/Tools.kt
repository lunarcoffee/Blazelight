package dev.lunarcoffee.blazelight.site.routes.tools

import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.toolsRoute() = get("/tools") {
    call.respondHtmlTemplate(HeaderBarTemplate(s.tools, call, s)) {
        content {
            breadcrumbs { thisCrumb(call, s.tools) }
            br()

            h3(classes = "title") { b { +s.tools } }
            hr()
            a(href = "/tools/help", classes = "a2") { +s.helpDescription }
        }
    }
}
