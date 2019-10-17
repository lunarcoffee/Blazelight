package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.forumsAddRoute() = get("/forums/add") {
    call.respondHtmlTemplate(HeaderBarTemplate("Add Forum", call)) {
        content {
            p { +"add forum" }
        }
    }
}
