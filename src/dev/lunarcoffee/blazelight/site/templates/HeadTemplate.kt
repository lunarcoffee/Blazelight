package dev.lunarcoffee.blazelight.site.templates

import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.padding
import dev.lunarcoffee.blazelight.site.std.toTimeTime
import io.ktor.html.*
import kotlinx.html.*
import java.time.ZoneId

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

            // Add the language-specific font if it exists.
            if (s.language.fontUrl != null)
                link(href = s.language.fontUrl, rel = "stylesheet")
        }
        body {
            insert(body)
            p(classes = "footer-text") {
                this@p.style = "text-align: center;"
                val timeZoneName = user?.settings?.zoneId?.id ?: ZoneId.systemDefault().id

                +"Times displayed are $timeZoneName."
                sep()
                a(href = GITHUB_LINK, target = "_blank", classes = "a2") { +"GitHub" }
                sep()
                +"Page loaded at ${System.currentTimeMillis().toTimeTime(user)}"
            }
            padding(10)
        }
    }

    private fun P.sep() = b(classes = "footer-sep") { +"/" }

    companion object {
        private const val GITHUB_LINK = "https://github.com/LunarCoffee/Blazelight"
    }
}
