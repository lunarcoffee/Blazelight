package dev.lunarcoffee.blazelight.site.routes.forums.threads

import dev.lunarcoffee.blazelight.model.api.categories.getCategory
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.shared.language.prep
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.*
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.html.*

fun Route.forumsViewThreadEdit() = get("/forums/view/{forumId}/{threadId}/edit") {
    val specialMessages = listOf(s.invalidTitle1To300, s.invalidContent1To10000)

    val params = call.parameters
    val messageIndex = params["a"]?.toIntOrNull()

    val forum = params["forumId"]?.toLongOrNull()?.getForum()
        ?: return@get call.respond(HttpStatusCode.NotFound)
    val thread = params["threadId"]?.toLongOrNull()?.getThread()
        ?: return@get call.respond(HttpStatusCode.NotFound)

    val template = HeaderBarTemplate("${s.thread} - ${thread.id} - ${s.editThreadCap}", call, s)
    call.respondHtmlTemplate(template) {
        content {
            breadcrumbs {
                val category = forum.categoryId.getCategory()!!
                crumb("/forums", s.forums)
                crumb("/forums/${category.name}#${category.name}", category.name)
                crumb("/forums/view/${forum.id}", forum.name)
                crumb(
                    "/forums/view/${forum.id}/${thread.id}",
                    "Thread: ${thread.title.textOrEllipsis(60)}"
                )
                thisCrumb(call, s.editThreadCap)
            }
            br()

            h3 {
                +s.editThread
                b { +s.entityIdFormat.prep(thread.id) }
                +":"
            }
            hr()
            form(action = call.path, method = FormMethod.post) {
                input(type = InputType.text, name = "title", classes = "fi-text fi-top") {
                    placeholder = s.title
                    value = thread.title
                }
                br()
                formattedTextInput(s, thread.firstPost!!.contentRaw)
                hr()
                input(type = InputType.submit, classes = "button-1") { value = s.save }

                if (messageIndex in specialMessages.indices)
                    span(classes = "red") { +specialMessages[messageIndex!!] }
            }

            // TODO: Attachments, emotes, thread icon.
        }
    }
}
