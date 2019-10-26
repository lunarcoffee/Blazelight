package dev.lunarcoffee.blazelight.shared

import java.io.File
import java.io.FileNotFoundException

object BlazelightConfig {
    val defaultStyle = "Glass Classic"
    val styles = File("resources/static/css").listFiles()!!.associate {
        val styleNameLine = it.readLines()[0]
        styleNameLine.substring(3, styleNameLine.lastIndex - 2) to it.name
    }

    fun load(path: String) {
        val text = try {
            File(path).readText()
        } catch (e: FileNotFoundException) {
            null
        }
    }
}
