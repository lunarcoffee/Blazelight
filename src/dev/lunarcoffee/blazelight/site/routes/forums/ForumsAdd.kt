package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.forumsAdd() = get("/forums/add") {
    val specialMessages = listOf(s.noPermissions, s.invalidName1To100, s.invalidTopic1To1000)
    val params = call.parameters

    val messageIndex = params["a"]?.toIntOrNull()
    val category = params["b"]?.toLongOrNull()?.getCategory()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    call.respondHtmlTemplate(HeaderBarTemplate(s.createForum, call, s)) {
        content {
            breadcrumbs {
                val name = category.name
                crumb("/forums", s.forums)
                crumb("/forums/$name#$name", name)
                thisCrumb(call, s.createForum)
            }
            br()

            h3(classes = "title") {
                +s.newForumHeading
                b { +category.name }
                +":"
            }
            hr()
            form(action = call.request.uri, method = FormMethod.post) {
                // Hidden category ID.
                input(type = InputType.hidden, name = "category") {
                    value = category.id.toString()
                }

                input(type = InputType.text, name = "name", classes = "fi-text fi-top") {
                    placeholder = s.name
                }
                br()
                input(type = InputType.text, name = "topic", classes = "fi-text") {
                    placeholder = s.topic
                }
                hr()
                input(type = InputType.submit, classes = "button-1") { value = s.create }

                // This message will be displayed upon a special forum creation event.
                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }
        }
    }
}
