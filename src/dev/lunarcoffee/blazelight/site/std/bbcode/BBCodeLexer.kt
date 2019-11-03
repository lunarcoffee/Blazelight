package dev.lunarcoffee.blazelight.site.std.bbcode

import java.util.*

// Reads BBCode formatting tokens from some raw post content.
class BBCodeLexer(val text: String) {
    private var pos = 0
    private var curChar = text[pos]
    private val peekReturnQueue: Queue<BBCodeToken> = LinkedList<BBCodeToken>()

    fun nextToken(prevIsBracket: Boolean = false): BBCodeToken {
        if (peekReturnQueue.isNotEmpty())
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
                        if (tagName == "url") {
                            // Skip closing bracket.
                            readNext()

                            // Use tag content as URL if no argument is provided.
                            val nextToken = peekToken()
                            if (nextToken is BcTText)
                                BcTOpenTag(tagName, tagArg.ifEmpty { nextToken.text })
                            // If the next token wasn't a [BcTText], that is an invalid URL tag, so
                            // just interpret it as some text.
                            else
                                BcTOpenTag(tagName, tagArg.ifEmpty { "a" })
                        } else {
                            BcTOpenTag(tagName, tagArg.ifEmpty { null }).also { readNext() }
                        }
                    } else {
                        BcTText("[$tagContent")
                    }
                }
            }
        } else {
            when (curChar) {
                '[' -> {
                    readNext()
                    nextToken(true)
                }
                '\u0000' -> BcTEof
                else -> BcTText(readWhile("""[^\[]""".toRegex()))
            }
        }
    }

    private fun peekToken(prevIsBracket: Boolean = false): BBCodeToken {
        val next = nextToken(prevIsBracket)
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
