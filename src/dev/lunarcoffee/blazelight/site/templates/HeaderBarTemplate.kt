package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.site.sessions.UserSession
import io.ktor.application.ApplicationCall
import io.ktor.html.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

class HeaderBarTemplate(
    private val titleText: String,
    private val call: ApplicationCall
) : Template<HTML> {

    val content = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        insert(HeadTemplate(titleText)) {
            body {
                div(classes = "header-bar") {
                    h1(classes = "header-bar-title") { +"Blazelight" }
                    div(classes = "header-bar-top-menu") {
                        if (call.sessions.get<UserSession>() == null) {
                            a(href = "/register", classes = "header-top-button") { +"Register" }
                            a(href = "/login", classes = "header-top-button") { +"Login" }
                        } else {
                            a(href = "/me", classes = "header-top-button") { +"My Profile" }
                            a(href = "/logout", classes = "header-top-button") { +"Log Out" }
                        }
                    }

                    div(classes = "header-bar-button-row") {
                        a(href = "/", classes = "header-bar-button hbb-left") { +"Home" }
                        a(href = "/forums", classes = "header-bar-button") { +"Forums" }
                        a(href = "/tools", classes = "header-bar-button hbb-right") { +"Tools" }
                    }
                }
                div(classes = "content") { insert(content) }
            }
        }
    }
}
