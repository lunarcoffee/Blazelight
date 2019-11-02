package dev.lunarcoffee.blazelight.shared.language

@Suppress("unused")
enum class Language(
    val languageName: String,
    val stringCode: String,
    private val code: Int,
    val fontUrl: String? = null
) {
    ENGLISH("English", "en-us", 0),
    MANDARIN(
        "\u4E2D\u6587", // Mandarin characters for "mandarin."
        "zh-hans",
        1,
        "https://fonts.googleapis.com/css?family=Noto+Sans+SC&display=swap"
    );

    // [LocalizedStrings] for the current language.
    val strings get() = LanguageManager.localizedStrings.find { it.language.code == code }!!
}
