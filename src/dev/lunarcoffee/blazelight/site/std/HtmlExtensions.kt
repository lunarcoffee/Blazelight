package dev.lunarcoffee.blazelight.site.std

import kotlinx.html.*

fun HtmlBlockTag.padding(height: Int) = div { style = "height: ${height}px" }
