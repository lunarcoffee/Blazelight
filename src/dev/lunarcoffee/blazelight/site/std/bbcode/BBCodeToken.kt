package dev.lunarcoffee.blazelight.site.std.bbcode

sealed class BBCodeToken

class BcTOpenTag(val name: String, val arg: String) : BBCodeToken()
class BcTCloseTag(val name: String) : BBCodeToken()
class BcTText(val text: String) : BBCodeToken()

object BcTEof : BBCodeToken()
