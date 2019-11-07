package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.padding
import dev.lunarcoffee.blazelight.site.std.path
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

fun Route.forumsCategoryDelete() = get("/forums/{categoryId}/delete") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val params = call.parameters

    // Ensure that the deleter is an administrator; only they can manage forums.
    if (!user.isAdmin)
        return@get call.respond(HttpStatusCode.Forbidden)

    val category = params["categoryId"]?.toLongOrNull()?.getCategory()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${category.name} - ${s.deleteCategory}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            val catName = category.name
            breadcrumbs {
                crumb("/forums", s.forums)
                crumb("/forums/$catName#$catName", catName)
                thisCrumb(call, s.deleteCategory)
            }
            br()

            h3 {
                +s.deleteCategoryTitle
                b { +category.name }
                +":"
            }
            hr()
            p { +s.deleteCategoryConfirmMessage }

            padding(20)
            a(href = "${call.path}/go", classes = "button-1") { +s.delete }
            a(href = "/forums/$catName#$catName", classes = "button-1") { +s.cancel }
            padding(8)
        }
    }
}
