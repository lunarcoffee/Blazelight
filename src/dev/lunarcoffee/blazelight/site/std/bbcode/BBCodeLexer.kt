package dev.lunarcoffee.blazelight.site.std.bbcode

import java.util.*

// Reads BBCode formatting tokens from some raw post content.
class BBCodeLexer(val text: String) {
    private var pos = 0
    private var curChar = text[pos]
    private val peekReturnQueue: Queue<BBCodeToken> = LinkedList<BBCodeToken>()

    fun nextToken(prevIsBracket: Boolean = false, skipQueue: Boolean = false): BBCodeToken {
        if (!skipQueue && peekReturnQueue.isNotEmpty())
            return peekReturnQueue.poll()

        return if (prevIsBracket) {
            when (curChar) {
                '/' -> {
                    // Skip slash.
                    readNext()

                    val tagName = readWhile("""[^]]""".toRegex())
                    if (tagName in TAG_NAMES)
                        BcTCloseTag(tagName).also { readNext() }
                    else
                        BcTText("[/$tagName")
                }
                ']' -> {
                    readNext()
                    BcTText("[]")
                }
                else -> {
                    val tagContent = readWhile("""[^]]""".toRegex())
                    val tagName = tagContent.substringBefore("=")
                    val tagArg = tagContent.substringAfter("=", "")

                    if (tagName in TAG_NAMES) {
                        readNext()
                        if (tagName in IMPLICIT_ARG_TAG_NAMES)
                            implicitArgTag(tagName, tagArg, tagContent)
                        else
                            BcTOpenTag(tagName, tagArg)
                    } else {
                        BcTText("[$tagContent")
                    }
                }
            }
        } else {
            when (curChar) {
                '[' -> {
                    readNext()
                    nextToken(true, skipQueue)
                }
                '\\' -> {
                    readNext()
                    BcTText(curChar.toString()).also { readNext() }
                }
                '\u0000' -> BcTEof
                else -> BcTText(readWhile("""[^\[\\]""".toRegex()))
            }
        }
    }

    // An "implicit arg" tag is one which has an optional argument which can either be specified
    // directly, or will be taken from the content within the tag. For instance, <url> is one such
    // tag, where an argument can be provided (<url=url_here>click here</url>), or not
    // (<url>url_here</url>, essentially converted to <url=url_here>url_here</url>).
    private fun implicitArgTag(tagName: String, tagArg: String, tagContent: String): BBCodeToken {
        val textTokens = mutableListOf<BcTText>()
        var nextToken = peekToken()
        var imageInUrl = false

        // Gather all text until the closing tag is met. This allows for formatting tags like <b>
        // or <i> to be applied to a part of the display content while still maintaining the full
        // implicit tag argument.
        while (nextToken !is BcTCloseTag || nextToken.name != tagName) {
            when (nextToken) {
                is BcTText -> textTokens += nextToken
                is BcTEof -> return BcTText("[$tagContent]")
                is BcTOpenTag -> if (nextToken.name == "img")
                    imageInUrl = tagName == "url"
            }
            nextToken = peekToken()
        }

        val text = textTokens.joinToString("") { it.text }
        return if (text.isEmpty())
            if (imageInUrl) BcTOpenTag(tagName, tagArg) else BcTText("[$tagContent][/$tagName]")
        else
            BcTOpenTag(tagName, tagArg.ifEmpty { text })
    }

    private fun peekToken(prevIsBracket: Boolean = false): BBCodeToken {
        val next = nextToken(prevIsBracket, true)
        peekReturnQueue.add(next)
        return next
    }

    private fun readWhile(regex: Regex): String {
        var string = ""
        while (curChar.toString() matches regex && curChar != '\u0000') {
            string += curChar
            readNext()
        }
        return string
    }

    private fun readNext() {
        pos++
        curChar = if (pos > text.lastIndex) '\u0000' else text[pos]
    }

    companion object {
        private val TAG_NAMES = setOf(
            "b", "i", "u", "s", "color", "size", "code", // Stylistic tags.
            "url", "email", "img", "quote", "hr" // Functional tags.
        )
        private val IMPLICIT_ARG_TAG_NAMES = setOf("url", "email", "img")
    }
}
