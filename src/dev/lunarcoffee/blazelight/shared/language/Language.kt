package dev.lunarcoffee.blazelight.shared.language

enum class Language(val languageName: String, val stringCode: String, val code: Int) {
    ENGLISH("English", "en-us", 0);
//    MANDARIN("\u4E2D\u6587", 1) // Mandarin characters for "mandarin."

    // [LocalizedStrings] for the current language.
    val strings get() = LanguageManager.localizedStrings.find { it.language.code == code }!!
}
