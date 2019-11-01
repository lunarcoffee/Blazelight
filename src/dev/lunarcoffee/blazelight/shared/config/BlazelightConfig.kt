package dev.lunarcoffee.blazelight.shared.config

import dev.lunarcoffee.blazelight.shared.language.LanguageManager
import java.io.File
import kotlin.properties.Delegates

class BlazelightConfig {
    lateinit var resourceRoot: String
    lateinit var defaultStyle: String
    lateinit var headerBarText: String
    lateinit var titleText: String
    lateinit var defaultLangCode: String

    var threadPageSize: Int by Delegates.notNull()
    var commentPageSize: Int by Delegates.notNull()

    val styles: Map<String, String> by lazy {
        File("$resourceRoot/static/css").listFiles()!!.associate {
            val styleNameLine = it.readLines()[0]
            styleNameLine.substring(3, styleNameLine.lastIndex - 2) to it.name
        }
    }

    // [LocalizedStrings] for default language.
    val ds
        get() = LanguageManager
            .localizedStrings
            .find { it.language.stringCode == defaultLangCode }!!
}
