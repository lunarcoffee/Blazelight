package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.formattedTextInput
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.forumsViewAdd() = get("/forums/view/{id}/add") {
    val specialMessages = listOf(s.invalidTitle1To300, s.invalidContent1To10000)

    val messageIndex = call.parameters["a"]?.toIntOrNull()
    val forum = call.parameters["id"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${s.forums} - ${forum.name} - ${s.addThread}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                thisCrumb(call, s.addThread)
            }
            br()

            h3 {
                +s.newThreadHeading
                b { +forum.name }
                +":"
            }
            hr()
            form(action = call.request.path(), method = FormMethod.post) {
                input(type = InputType.text, name = "title", classes = "fi-text fi-top") {
                    placeholder = s.title
                }
                br()
                formattedTextInput(s)
                hr()
                input(type = InputType.submit, classes = "button-1") { value = s.create }

                // This message will be displayed upon a special thread creation event.
                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }

            // TODO: Attachments, emotes, thread icon.
        }
    }
}
