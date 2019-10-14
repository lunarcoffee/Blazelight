package dev.lunarcoffee.blazelight.std

object LanguageManager {
    fun toLanguage(str: String) = Language.values()[str.toInt()]!!
}
