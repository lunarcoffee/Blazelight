package dev.lunarcoffee.blazelight.shared.language

object LanguageManager {
    fun toLanguage(str: String) = Language.values()[str.toInt()]!!
}
