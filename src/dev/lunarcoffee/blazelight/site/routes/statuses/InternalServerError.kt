package dev.lunarcoffee.blazelight.site.routes.statuses

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import kotlinx.html.*

fun StatusPages.Configuration.internalServerErrorStatus() {
    status(HttpStatusCode.InternalServerError) {
        call.respondHtmlTemplate(HeaderBarTemplate("Internal Server Error", call)) {
            content {
                h3 { +"Internal server error:" }
                hr()
                p {
                    +"The remote server is currently experiencing a malfunction. "
                    +"Please try again later."
                }
            }
        }
    }
}
