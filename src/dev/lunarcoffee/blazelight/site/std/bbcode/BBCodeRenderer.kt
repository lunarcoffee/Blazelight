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
            else -> renderBBCodeByTokens()
        }
    }

    private fun HtmlInlineTag.renderBBCodeByTokens() {
        while (index <= tokens.lastIndex) {
            when (val token = tokens[index]) {
                is BcTText -> renderWithNewlines(token.text)
                is BcTOpenTag -> renderBBCodeTag(token)
                is BcTCloseTag -> return
            }
            index++
        }
    }
}
