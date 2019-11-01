package dev.lunarcoffee.blazelight.site.std.bbcode

sealed class BBCodeToken

data class BcTOpenTag(val name: String) : BBCodeToken()
data class BcTCloseTag(val name: String) : BBCodeToken()
data class BcTText(val text: String) : BBCodeToken()

object BcTEof : BBCodeToken()
