package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.homeRoute() = get("/") {
    call.respondHtmlTemplate(HeaderBarTemplate("Home", call)) {
        content {
            p { +"Welcome to Blazelight!" }
        }
    }
}
