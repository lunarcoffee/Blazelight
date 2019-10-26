package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.shared.BlazelightConfig
import io.ktor.html.*
import kotlinx.html.*

class HeadTemplate(private val titleText: String, private val user: User?) : Template<HTML> {
    val body = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        val cssStyle = user?.settings?.theme ?: BlazelightConfig.defaultStyle
        head {
            meta(charset = "utf-8")
            title(content = "Blazelight - $titleText")
            link(
                href = "/css/${BlazelightConfig.styles[cssStyle]}",
                rel = "stylesheet",
                type = "text/css"
            )
        }
        body { insert(body) }
    }
}
