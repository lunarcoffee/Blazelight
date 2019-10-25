package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.shared.Language
import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

private val specialMessages = listOf(
    "That email is invalid!",
    "That username is invalid! It must be at most 40 characters long (inclusive).",
    "That password is invalid! It must be 8 to 200 characters long (inclusive).",
    "That email is taken!",
    "That username is taken!",
    "The two passwords don't match!",
    "Success! You may now log in."
)

fun Routing.registerRoute() = get("/register") {
    val messageIndex = call.parameters["a"]?.toIntOrNull()

    call.respondHtmlTemplate(HeaderBarTemplate("Register", call)) {
        content {
            breadcrumbs { crumb("/register", "Register") }
            br()

            h3 { +"Register for an account:" }
            hr()
            form(action = "/register", method = FormMethod.post) {
                input(type = InputType.text, name = "email", classes = "fi-text") {
                    placeholder = "Email"
                }
                br()
                input(type = InputType.text, name = "username", classes = "fi-text") {
                    placeholder = "Username"
                }
                br()
                input(type = InputType.password, name = "password", classes = "fi-text") {
                    placeholder = "Password"
                }
                br()
                input(type = InputType.password, name = "password-c", classes = "fi-text") {
                    placeholder = "Retype password"
                }
                hr()
                select(classes = "fi-select") {
                    name = "timeZone"
                    for ((index, zoneId) in TimeZoneManager.timeZones.withIndex()) {
                        option {
                            // Select UTC time by default (offset 0).
                            if (index == 14)
                                selected = true
                            value = index.toString()
                            +zoneId.id.substringAfter("/").replace("GMT", "UTC")
                        }
                    }
                }
                +"(time zone)"
                br()
                select(classes = "fi-select") {
                    name = "language"
                    for ((index, lang) in Language.values().withIndex()) {
                        option {
                            value = index.toString()
                            +lang.languageName
                        }
                    }
                }
                +"(language)"
                hr()
                input(type = InputType.submit, classes = "button-1") { value = "Register" }

                // This message will be displayed after attempting to register.
                val color = if (messageIndex == specialMessages.lastIndex) "green" else "red"
                if (messageIndex in specialMessages.indices)
                    span(classes = color) { +specialMessages[messageIndex!!] }
            }
        }
    }
}
