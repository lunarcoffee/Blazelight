package dev.lunarcoffee.blazelight.routes.templates

import io.ktor.html.*
import kotlinx.html.*

class HeaderBarTemplate(private val titleText: String) : Template<HTML> {
    val content = Placeholder<HtmlBlockTag>()
    val personalized = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        insert(HeadTemplate(titleText)) {
            body {
                div(classes = "header-bar") {
                    h1(classes = "header-bar-title") { +"Blazelight" }
                    div(classes = "header-bar-top-menu") { insert(personalized) }

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
