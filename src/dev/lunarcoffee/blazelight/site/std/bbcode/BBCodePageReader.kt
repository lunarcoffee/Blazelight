package dev.lunarcoffee.blazelight.site.std.bbcode

import java.io.File

class BBCodePageReader(path: String) {
    private val file = File(path)

    lateinit var topicName: String
    lateinit var topicText: String

    fun read(): Boolean {
        if (!file.exists())
            return false

        val topicTextLines = file.readLines()
        topicName = topicTextLines[0].substringAfter("#").trim()
        topicText = topicTextLines.drop(1).joinToString("\n")

        return true
    }
}
