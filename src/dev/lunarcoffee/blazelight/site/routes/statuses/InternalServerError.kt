package dev.lunarcoffee.blazelight.site.routes.statuses

import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import kotlinx.html.*

fun StatusPages.Configuration.internalServerErrorStatus() {
    status(HttpStatusCode.InternalServerError) {
        call.respondHtmlTemplate(HeaderBarTemplate(s.internalServerError, call, s)) {
            content {
                h3 { b { +s.internalServerErrorHeading } }
                hr()
                p { +s.internalServerErrorNotice }
            }
        }
    }
}
