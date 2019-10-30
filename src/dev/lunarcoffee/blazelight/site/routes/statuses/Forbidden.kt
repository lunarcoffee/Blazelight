package dev.lunarcoffee.blazelight.site.routes.statuses

import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import kotlinx.html.*

fun StatusPages.Configuration.forbiddenStatus() = status(HttpStatusCode.Forbidden) {
    call.respondHtmlTemplate(HeaderBarTemplate("Forbidden", call)) {
        content {
            h3 { b { +"Forbidden:" } }
            hr()
            p { +"You do not have enough privileges to perform that action." }
        }
    }
}
