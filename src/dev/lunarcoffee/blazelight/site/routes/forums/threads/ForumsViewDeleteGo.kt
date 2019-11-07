package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.ForumDeleteManager
import dev.lunarcoffee.blazelight.model.api.forums.getForum
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

fun Route.forumsViewDeleteGo() = get("/forums/view/{forumId}/delete/go") {
    val params = call.parameters

    // Ensure that the deleter is an administrator.
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    if (!user.isAdmin)
        return@get call.respond(HttpStatusCode.Forbidden)

    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    ForumDeleteManager.delete(forum)
    val categoryName = forum.categoryId.getCategory()!!.name
    call.respondRedirect("/forums/$categoryName#$categoryName")
}
