package dev.lunarcoffee.blazelight.site.templates

import io.ktor.html.*
import kotlinx.html.*

class HeadTemplate(private val titleText: String) : Template<HTML> {
    val body = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        head {
            meta(charset = "utf-8")
            title(content = "Blazelight - $titleText")
            link(href = "/css/style_glass.css", rel = "stylesheet", type = "text/css")
        }
        body { insert(body) }
    }
}
