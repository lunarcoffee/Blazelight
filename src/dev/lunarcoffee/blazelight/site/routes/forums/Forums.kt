package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.forums.CategoryManager
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.forumsRoute() = get("/forums") {
    val categories = CategoryManager.getAll()

    call.respondHtmlTemplate(HeaderBarTemplate("Forums", call)) {
        content {
            for (category in categories)
                p { +"${category.name} on ${category.creationTime}" }
        }
    }
}
