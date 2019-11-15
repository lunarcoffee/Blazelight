package dev.lunarcoffee.blazelight.site.std.bbcode

import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.renderWithNewlines
import kotlinx.html.*

class BBCodeRenderer(private val rootTag: HtmlBlockInlineTag, private val s: LocalizedStrings) {
    private var index = 0 // Index of the token currently being evaluated.
    private lateinit var tokens: List<BBCodeToken>

    fun render(text: String, internal: Boolean) {
        tokens = BBCodeParser(BBCodeLexer(text)).getTokens()
        rootTag.renderByTokens(internal = internal)
    }

    private fun HtmlBlockInlineTag.renderBBCodeTag(
        tag: BcTOpenTag,
        preserveFormatting: Boolean,
        internal: Boolean = false
    ) {
        index++ // Skip the opening [tag].
        if (internal && tag.name == "hr") {
            hr()
            renderByTokens(preserveFormatting, internal)
            return
        }

        when (tag.name) {
            "b" -> b { renderByTokens(preserveFormatting, internal) }
            "i" -> i { renderByTokens(preserveFormatting, internal) }
            "u" -> ins { renderByTokens(preserveFormatting, internal) }
            "s" -> del { renderByTokens(preserveFormatting, internal) }
            "color" -> span {
                style = "color: ${tag.arg.substringBefore(";")};"
                renderByTokens(preserveFormatting, internal)
            }
            "size" -> span {
                val size = tag.arg.toIntOrNull()
                val coercedSize = size?.coerceIn(20, 200) ?: 100
                val actualSize = if (internal) size else coercedSize

                style = "font-size: $actualSize%;"
                renderByTokens(preserveFormatting, internal)
            }
            "code" -> {
                val internalClass = if (internal) "in-block" else ""
                div(classes = "code comment-text $internalClass") { code { renderByTokens(true) } }
            }
            "url" -> a(href = tag.arg, target = "_blank", classes = "u") {
                renderByTokens(preserveFormatting, internal)
            }
            "email" -> a(href = "mailto:${tag.arg}", classes = "u") {
                renderByTokens(preserveFormatting, internal)
            }
            "img" -> a(href = tag.arg) { img(src = tag.arg, classes = "post-image") }
            "quote" -> {
                val internalClass = if (internal) "in-block" else ""
                blockQuote(classes = "comment-text $internalClass") {
                    b(classes = "px") {
                        +if (tag.arg.isEmpty()) "Quote:" else "${tag.arg} ${s.said}:"
                    }
                    br()
                    this@renderBBCodeTag.renderByTokens(preserveFormatting, internal)
                }
            }
            else -> renderByTokens(preserveFormatting, internal)
        }
    }

    private fun HtmlBlockInlineTag.renderByTokens(
        preserveAllFormatting: Boolean = false,
        internal: Boolean = false
    ) {
        while (index <= tokens.lastIndex) {
            when (val token = tokens[index]) {
                is BcTText -> renderWithNewlines(token.text, preserveAllFormatting)
                is BcTOpenTag -> renderBBCodeTag(token, preserveAllFormatting, internal)
                is BcTCloseTag -> return
            }
            index++
        }
    }
}
