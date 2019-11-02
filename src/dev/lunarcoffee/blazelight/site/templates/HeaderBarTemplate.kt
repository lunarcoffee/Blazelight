package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.application.ApplicationCall
import io.ktor.html.*
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*

class HeaderBarTemplate(
    private val titleText: String,
    private val call: ApplicationCall,
    private val s: LocalizedStrings
) : Template<HTML> {

    val content = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        insert(HeadTemplate(titleText, call.sessions.get<UserSession>()?.getUser(), s)) {
            body {
                div(classes = "header-bar") {
                    h1(classes = "header-bar-title") { +BL_CONFIG.headerBarText }
                    div(classes = "header-bar-top-menu") {
                        val user = call.sessions.get<UserSession>()?.getUser()
                        if (user == null) {
                            a(href = "/register", classes = "header-top-button") { +s.register }
                            a(href = "/login", classes = "header-top-button") { +s.login }
                        } else {
                            a(href = "/users/${user.id}", classes = "header-top-button") {
                                +s.myProfile
                            }
                            a(href = "/logout", classes = "header-top-button") { +s.logOut }
                        }
                    }

                    div(classes = "header-bar-button-row") {
                        div(classes = "header-bar-lp")
                        a(href = "/", classes = "header-bar-button hbb-left") { +s.home }
                        a(href = "/forums", classes = "header-bar-button") { +s.forums }
                        a(href = "/tools", classes = "header-bar-button hbb-right") { +s.tools }
                    }
                }
                div(classes = "content") { insert(content) }
            }
        }
    }
}
