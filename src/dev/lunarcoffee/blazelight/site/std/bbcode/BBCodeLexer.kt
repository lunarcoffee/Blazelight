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
                        if (tagName == "url")
                            urlTag(tagName, tagArg, tagContent)
                        else
                            BcTOpenTag(tagName, tagArg.ifEmpty { null })
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
                '\u0000' -> BcTEof
                else -> BcTText(readWhile("""[^\[]""".toRegex()))
            }
        }
    }

    private fun urlTag(tagName: String, tagArg: String, tagContent: String): BBCodeToken {
        val textTokens = mutableListOf<BcTText>()
        var nextToken = peekToken()

        // Gather all text until the closing URL tag is met. This allows for formatting tags like
        // <b> or <i> to be applied to a part of a link while still maintaining the full URL.
        while (nextToken !is BcTCloseTag || nextToken.name != "url") {
            if (nextToken is BcTText)
                textTokens += nextToken
            else if (nextToken is BcTEof)
                return BcTText("[$tagContent]")
            nextToken = peekToken()
        }

        val text = textTokens.joinToString("") { it.text }
        return BcTOpenTag(tagName, tagArg.ifEmpty { text.ifEmpty { null } })
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
        private val TAG_NAMES = setOf("b", "i", "u", "s", "url")
    }
}
