package dev.lunarcoffee.blazelight.site.std.bbcode

import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.renderWithNewlines
import kotlinx.html.*

class BBCodeRenderer(private val rootTag: HtmlBlockInlineTag, private val s: LocalizedStrings) {
    private var index = 0 // Index of the token currently being evaluated.
    private lateinit var tokens: List<BBCodeToken>

    fun render(text: String) {
        tokens = BBCodeParser(BBCodeLexer(text)).getTokens()
        rootTag.renderByTokens()
    }

    private fun HtmlBlockInlineTag.renderBBCodeTag(tag: BcTOpenTag, preserveFormatting: Boolean) {
        index++ // Skip the opening [tag].
        when (tag.name) {
            "b" -> b { renderByTokens(preserveFormatting) }
            "i" -> i { renderByTokens(preserveFormatting) }
            "u" -> ins { renderByTokens(preserveFormatting) }
            "s" -> del { renderByTokens(preserveFormatting) }
            "color" -> span {
                style = "color: ${tag.arg.substringBefore(";")};"
                renderByTokens(preserveFormatting)
            }
            "size" -> span {
                val coercedSize = tag.arg.toIntOrNull()?.coerceIn(20, 200) ?: 100
                style = "font-size: $coercedSize%;"
                renderByTokens(preserveFormatting)
            }
            "code" -> div(classes = "code comment-text") { code { renderByTokens(true) } }
            "url" -> a(href = tag.arg, target = "_blank", classes = "u") {
                renderByTokens(preserveFormatting)
            }
            "email" -> a(href = "mailto:${tag.arg}", classes = "u") {
                renderByTokens(preserveFormatting)
            }
            "img" -> a(href = tag.arg) { img(src = tag.arg, classes = "post-image") }
            "quote" -> blockQuote(classes = "comment-text") {
                b(classes = "px") { +if (tag.arg.isEmpty()) "Quote:" else "${tag.arg} ${s.said}:" }
                br()
                this@renderBBCodeTag.renderByTokens(preserveFormatting)
            }
            else -> renderByTokens(preserveFormatting)
        }
    }

    private fun HtmlBlockInlineTag.renderByTokens(preserveAllFormatting: Boolean = false) {
        while (index <= tokens.lastIndex) {
            when (val token = tokens[index]) {
                is BcTText -> renderWithNewlines(token.text, preserveAllFormatting)
                is BcTOpenTag -> renderBBCodeTag(token, preserveAllFormatting)
                is BcTCloseTag -> return
            }
            index++
        }
    }
}
