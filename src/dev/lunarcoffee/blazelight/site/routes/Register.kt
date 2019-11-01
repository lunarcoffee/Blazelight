package dev.lunarcoffee.blazelight.site.routes

import dev.lunarcoffee.blazelight.shared.TimeZoneManager
import dev.lunarcoffee.blazelight.shared.language.Language
import dev.lunarcoffee.blazelight.shared.language.s
import dev.lunarcoffee.blazelight.site.std.breadcrumbs.breadcrumbs
import dev.lunarcoffee.blazelight.site.templates.HeaderBarTemplate
import io.ktor.application.call
import io.ktor.html.respondHtmlTemplate
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

fun Routing.registerRoute() = get("/register") {
    val specialMessages = listOf(
        s.invalidEmail,
        s.invalidUsername1To40,
        s.invalidPassword8To1000,
        s.emailTaken,
        s.usernameTaken,
        s.passwordConfirmFail,
        s.successRegister
    )
    val messageIndex = call.parameters["a"]?.toIntOrNull()

    call.respondHtmlTemplate(HeaderBarTemplate(s.register, call, s)) {
        content {
            breadcrumbs { thisCrumb(call, s.register) }
            br()

            h3 { b { +s.registrationHeading } }
            hr()
            form(action = "/register", method = FormMethod.post) {
                input(type = InputType.text, name = "email", classes = "fi-text") {
                    placeholder = s.email
                }
                br()
                input(type = InputType.text, name = "username", classes = "fi-text") {
                    placeholder = s.username
                }
                br()
                input(type = InputType.password, name = "password", classes = "fi-text") {
                    placeholder = s.password
                }
                br()
                input(type = InputType.password, name = "password-c", classes = "fi-text") {
                    placeholder = s.retypePassword
                }
                hr()
                select(classes = "fi-select") {
                    name = "timeZone"
                    for ((index, zoneId) in TimeZoneManager.timeZones.withIndex()) {
                        option {
                            value = index.toString()
                            +zoneId.id
                        }
                    }
                }
                +s.timeZoneParen
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
                +s.languageParen
                hr()
                input(type = InputType.submit, classes = "button-1") { value = s.register }

                // This message will be displayed after attempting to register.
                val color = if (messageIndex == specialMessages.lastIndex) "green" else "red"
                if (messageIndex in specialMessages.indices)
                    span(classes = color) { +specialMessages[messageIndex!!] }
            }
        }
    }
}
