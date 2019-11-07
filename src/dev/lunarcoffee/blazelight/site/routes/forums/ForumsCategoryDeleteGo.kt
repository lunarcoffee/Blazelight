package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryDeleteManager
import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Route.forumsCategoryDeleteGo() = get("/forums/{categoryId}/delete/go") {
    val params = call.parameters

    // Ensure that the deleter is an administrator.
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    if (!user.isAdmin)
        return@get call.respond(HttpStatusCode.Forbidden)

    val category = params["categoryId"]?.toLongOrNull()?.getCategory()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    CategoryDeleteManager.delete(category)
    call.respondRedirect("/forums")
}
