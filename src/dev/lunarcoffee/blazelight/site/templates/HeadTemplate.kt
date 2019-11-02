package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import io.ktor.html.*
import kotlinx.html.*

class HeadTemplate(
    private val titleText: String,
    private val user: User?,
    private val s: LocalizedStrings
) : Template<HTML> {

    val body = Placeholder<HtmlBlockTag>()

    override fun HTML.apply() {
        val style = user?.settings?.theme ?: BL_CONFIG.defaultStyle
        head {
            meta(charset = "utf-8")
            title(content = "${BL_CONFIG.titleText} - $titleText")
            link(href = "/css/${BL_CONFIG.styles[style]}", rel = "stylesheet", type = "text/css")

            if (s.languageCode == "zh-hans") {
                link(
                    href = "https://fonts.googleapis.com/css?family=Noto+Sans+SC&display=swap",
                    rel = "stylesheet"
                )
            }
        }
        body { insert(body) }
    }
}
