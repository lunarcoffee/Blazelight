package dev.lunarcoffee.blazelight.shared.config

import org.yaml.snakeyaml.Yaml
import java.io.File

lateinit var BL_CONFIG: BlazelightConfig

fun loadBlazelightConfig(path: String?) {
    BL_CONFIG = if (path == null) {
        BlazelightConfig().apply {
            resourceRoot = "resources"
            defaultStyle = "Glass Classic"
            headerBarText = "Blazelight"
            titleText = headerBarText
        }
    } else {
        Yaml().loadAs(File(path).reader(), BlazelightConfig::class.java)
    }
}
