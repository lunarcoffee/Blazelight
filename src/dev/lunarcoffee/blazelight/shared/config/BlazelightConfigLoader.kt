package dev.lunarcoffee.blazelight.shared.config

import org.yaml.snakeyaml.Yaml
import java.io.File

lateinit var BL_CONFIG: BlazelightConfig

fun loadBlazelightConfig(path: String?) {
    BL_CONFIG = if (path == null) {
        BlazelightConfig().apply {
            resourceRoot = "resources"
            defaultStyle = "Grey Medium"
            headerBarText = "Blazelight"
            titleText = headerBarText

            threadPageSize = 15
            commentPageSize = 15
            defaultLangCode = "en-us"
        }
    } else {
        Yaml().loadAs(File(path).reader(), BlazelightConfig::class.java)
    }
}
