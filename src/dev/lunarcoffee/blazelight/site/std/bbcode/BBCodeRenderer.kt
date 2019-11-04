package dev.lunarcoffee.blazelight.site.std.bbcode

import dev.lunarcoffee.blazelight.site.std.renderWithNewlines
import kotlinx.html.*

class BBCodeRenderer(private val rootTag: HtmlInlineTag) {
    private var index = 0 // Index of the token currently being evaluated.
    private lateinit var tokens: List<BBCodeToken>

    fun render(text: String) {
        tokens = BBCodeParser(BBCodeLexer(text)).getTokens()
        rootTag.renderBBCodeByTokens()
    }

    private fun HtmlInlineTag.renderBBCodeTag(tag: BcTOpenTag) {
        index++ // Skip the opening [tag].
        when (tag.name) {
            "b" -> b { renderBBCodeByTokens() }
            "i" -> i { renderBBCodeByTokens() }
            "u" -> span {
                style = "text-decoration: underline;"
                renderBBCodeByTokens()
            }
            "s" -> span {
                style = "text-decoration: line-through;"
                renderBBCodeByTokens()
            }
            "url" -> a(href = tag.arg, target = "_blank", classes = "u") { renderBBCodeByTokens() }
            "email" -> a(href = "mailto:${tag.arg}", classes = "u") { renderBBCodeByTokens() }
            "color" -> span {
                style = "color: ${tag.arg.substringBefore(";")};"
                renderBBCodeByTokens()
            }
            "size" -> span {
                val coercedSize = tag.arg.toIntOrNull()?.coerceIn(20, 200) ?: 100
                style = "font-size: $coercedSize%;"
                renderBBCodeByTokens()
            }
            "code" -> span {
                style = "font-family: monospace;"
                renderBBCodeByTokens(true)
            }
            "img" -> img(src = tag.arg)
            else -> renderBBCodeByTokens()
        }
    }

    private fun HtmlInlineTag.renderBBCodeByTokens(preserveAllFormatting: Boolean = false) {
        while (index <= tokens.lastIndex) {
            when (val token = tokens[index]) {
                is BcTText -> renderWithNewlines(token.text, preserveAllFormatting)
                is BcTOpenTag -> renderBBCodeTag(token)
                is BcTCloseTag -> return
            }
            index++
        }
    }
}
