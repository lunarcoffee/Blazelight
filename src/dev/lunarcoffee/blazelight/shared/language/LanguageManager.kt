package dev.lunarcoffee.blazelight.shared.language

import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import org.yaml.snakeyaml.Yaml
import java.io.File

object LanguageManager {
    val localizedStrings = Language.values().map {
        val config = File("${BL_CONFIG.resourceRoot}/lang/${it.stringCode}.yaml")
        Yaml().loadAs(config.reader(), LocalizedStrings::class.java)
    }

    fun toLanguage(code: Int) = Language.values()[code]
    fun toLanguage(stringCode: String) = Language.values().find { it.stringCode == stringCode }!!
}
