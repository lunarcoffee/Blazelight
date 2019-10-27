package dev.lunarcoffee.blazelight.shared.config

import java.io.File

class BlazelightConfig {
    lateinit var resourceRoot: String
    lateinit var defaultStyle: String
    lateinit var headerBarText: String
    lateinit var titleText: String

    val styles: Map<String, String> by lazy {
        File("$resourceRoot/static/css").listFiles()!!.associate {
            val styleNameLine = it.readLines()[0]
            styleNameLine.substring(3, styleNameLine.lastIndex - 2) to it.name
        }
    }
}
