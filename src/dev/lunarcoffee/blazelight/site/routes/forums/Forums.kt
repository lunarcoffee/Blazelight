package dev.lunarcoffee.blazelight.site.routes.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryCache
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.std.plusButton
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.coroutines.runBlocking
import kotlinx.html.*

fun Routing.forumsRoute() = get("/forums/{category?}") {
    val specialMessages = listOf(s.invalidName1To100, s.noPermissions, s.successForum)

    val categories = CategoryCache.categories
    val messageIndex = call.parameters["a"]?.toIntOrNull()

    val user = call.sessions.get<UserSession>()?.getUser()
    val isAdmin = user?.isAdmin == true

    call.respondHtmlTemplate(HeaderBarTemplate(s.forums, call, s)) {
        content {
            val crumbName = call.parameters["category"]?.substringAfterLast("#")
            breadcrumbs {
                crumb("/forums", s.forums)

                // Add a category breadcrumb if the user has selected a category.
                if (categories.any { it.name == crumbName })
                    crumb("/forums/$crumbName#$crumbName", crumbName!!)
            }
            br()

            if (categories.isEmpty())
                p { +s.noCategories }

            for ((index, category) in categories.withIndex()) {
                div(classes = if (index == categories.lastIndex && !isAdmin) "" else "category") {
                    a(href = "/forums/${category.name}#${category.name}", classes = "a1") {
                        h3(classes = "title") {
                            id = category.name
                            b {
                                // Highlight the user's selected category (if one is selected).
                                if (category.name == crumbName)
                                    span(classes = "red red-u") { +category.name }
                                else
                                    +category.name
                            }
                            if (isAdmin)
                                plusButton("/forums/add?b=${category.id}", s.newForum)
                        }
                    }
                    hr()

                    if (category.forumIds.isEmpty())
                        p { +s.noForumsInCategory }

                    for (id in category.forumIds) {
                        val forum = runBlocking { id.getForum()!! }
                        div(classes = "forum-list-item") {
                            a(href = "/forums/view/$id", classes = "a1") {
                                +forum.name
                                +" (${forum.threadIds.size})"
                            }
                            p(classes = "forum-topic") { +forum.topic }
                            hr(classes = "hr-dot")
                        }
                    }
                }
            }

            if (isAdmin) {
                if (categories.isEmpty())
                    hr()

                h3 { +s.newCategoryHeading }
                form(action = "/forums/category", method = FormMethod.post, classes = "f-inline") {
                    input(type = InputType.text, name = "name", classes = "fi-text fi-top") {
                        placeholder = s.name
                    }
                    input(type = InputType.submit, classes = "button-1 b-inline") {
                        value = s.create
                    }

                    // This message will be displayed after an attempt to create a new category.
                    val color = if (messageIndex == specialMessages.lastIndex) "green" else "red"
                    if (messageIndex in specialMessages.indices)
                        span(classes = color) { +specialMessages[messageIndex!!] }
                }
            }
        }
    }
}
