package dev.lunarcoffee.blazelight.site.std.bbcode

class BBCodeParser(private val lexer: BBCodeLexer) {
    fun getTokens() = readSimplifyTokens()

    private fun readSimplifyTokens(): List<BBCodeToken> {
        val tokens = mutableListOf<BBCodeToken>()
        var curToken = lexer.nextToken()

        while (curToken !is BcTEof) {
            tokens += curToken
            curToken = lexer.nextToken()
        }

        // Merge each adjacent [BcTText] token into one. This is necessary because the lexer will
        // sometimes give many of the text tokens next to each other (which can be merged).
        return tokens.fold(listOf()) { acc, v ->
            val last = acc.lastOrNull()
            if (v is BcTText && last is BcTText)
                acc.dropLast(1) + BcTText(last.text + v.text)
            else
                acc + v
        }
    }
}
