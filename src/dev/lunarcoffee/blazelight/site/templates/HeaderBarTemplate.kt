package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
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
        insert(HeadTemplate(titleText, call.sessions.get<UserSession>()?.getUser())) {
            body {
                div(classes = "header-bar") {
                    h1(classes = "header-bar-title") { +BL_CONFIG.headerBarText }
                    div(classes = "header-bar-top-menu") {
                        val user = call.sessions.get<UserSession>()?.getUser()
                        if (user == null) {
                            a(href = "/register", classes = "header-top-button") { +"Register" }
                            a(href = "/login", classes = "header-top-button") { +"Login" }
                        } else {
                            a(href = "/users/${user.id}", classes = "header-top-button") {
                                +"My Profile"
                            }
                            a(href = "/logout", classes = "header-top-button") { +"Log Out" }
                        }
                    }

                    div(classes = "header-bar-button-row") {
                        div(classes = "header-bar-lp")
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
