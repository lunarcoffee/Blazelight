package dev.lunarcoffee.blazelight.shared

private val ILLEGAL_CHARS_REGEX = "[\u0000-\u001F\u007F-\u009F\u200B-\u200F\u2060\uFEFF]".toRegex()

fun String.sanitize() = replace(ILLEGAL_CHARS_REGEX, "").trim()
