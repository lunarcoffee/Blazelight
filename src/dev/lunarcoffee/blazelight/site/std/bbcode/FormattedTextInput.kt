package dev.lunarcoffee.blazelight.site.std.bbcode

import kotlinx.html.*

fun HtmlBlockTag.formattedTextInput() = textArea(rows = "8", classes = "fti") {
    name = "content"
    placeholder = "Type something..."
}
