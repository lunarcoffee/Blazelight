package dev.lunarcoffee.blazelight.site.std.bbcode

import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.renderWithNewlines
import kotlinx.html.*

class BBCodeRenderer(private val rootTag: HtmlBlockInlineTag, private val s: LocalizedStrings) {
    private var index = 0 // Index of the token currently being evaluated.
    private lateinit var tokens: List<BBCodeToken>

    fun render(text: String, allowHr: Boolean) {
        tokens = BBCodeParser(BBCodeLexer(text)).getTokens()
        rootTag.renderByTokens(allowHr = allowHr)
    }

    private fun HtmlBlockInlineTag.renderBBCodeTag(
        tag: BcTOpenTag,
        preserveFormatting: Boolean,
        allowHr: Boolean = false
    ) {
        index++ // Skip the opening [tag].
        if (allowHr && tag.name == "hr") {
            hr()
            renderByTokens(preserveFormatting, allowHr)
        }

        when (tag.name) {
            "b" -> b { renderByTokens(preserveFormatting, allowHr) }
            "i" -> i { renderByTokens(preserveFormatting, allowHr) }
            "u" -> ins { renderByTokens(preserveFormatting, allowHr) }
            "s" -> del { renderByTokens(preserveFormatting, allowHr) }
            "color" -> span {
                style = "color: ${tag.arg.substringBefore(";")};"
                renderByTokens(preserveFormatting, allowHr)
            }
            "size" -> span {
                val coercedSize = tag.arg.toIntOrNull()?.coerceIn(20, 200) ?: 100
                style = "font-size: $coercedSize%;"
                renderByTokens(preserveFormatting, allowHr)
            }
            "code" -> div(classes = "code comment-text") { code { renderByTokens(true) } }
            "url" -> a(href = tag.arg, target = "_blank", classes = "u") {
                renderByTokens(preserveFormatting, allowHr)
            }
            "email" -> a(href = "mailto:${tag.arg}", classes = "u") {
                renderByTokens(preserveFormatting, allowHr)
            }
            "img" -> a(href = tag.arg) { img(src = tag.arg, classes = "post-image") }
            "quote" -> blockQuote(classes = "comment-text") {
                b(classes = "px") { +if (tag.arg.isEmpty()) "Quote:" else "${tag.arg} ${s.said}:" }
                br()
                this@renderBBCodeTag.renderByTokens(preserveFormatting, allowHr)
            }
            else -> renderByTokens(preserveFormatting, allowHr)
        }
    }

    private fun HtmlBlockInlineTag.renderByTokens(
        preserveAllFormatting: Boolean = false,
        allowHr: Boolean = false
    ) {
        while (index <= tokens.lastIndex) {
            when (val token = tokens[index]) {
                is BcTText -> renderWithNewlines(token.text, preserveAllFormatting)
                is BcTOpenTag -> renderBBCodeTag(token, preserveAllFormatting, allowHr)
                is BcTCloseTag -> return
            }
            index++
        }
    }
}
