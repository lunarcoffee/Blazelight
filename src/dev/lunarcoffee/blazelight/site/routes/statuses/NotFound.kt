package dev.lunarcoffee.blazelight.site.routes.statuses

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import kotlinx.html.*

fun StatusPages.Configuration.notFoundStatus() = status(HttpStatusCode.NotFound) {
    call.respondHtmlTemplate(HeaderBarTemplate("Not Found", call)) {
        content {
            h3 { +"Not found:" }
            hr()
            p { +"The content you requested could not be found. Ensure that the URL is correct." }
        }
    }
}
