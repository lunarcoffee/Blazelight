package dev.lunarcoffee.blazelight.site.std

import kotlinx.html.*

fun HtmlBlockTag.padding(height: Int) = div { style = "height: ${height}px;" }
fun HtmlBlockTag.fillX() = div { style = "width: available;" }

fun String.textOrEllipsis(limit: Int) = take(limit) + if (length > limit) "..." else ""
