package dev.lunarcoffee.blazelight.site.std

import dev.lunarcoffee.blazelight.shared.language.LocalizedStrings
import dev.lunarcoffee.blazelight.site.std.bbcode.BBCodeRenderer
import io.ktor.application.ApplicationCall
import io.ktor.request.path
import kotlinx.html.*

fun String.textOrEllipsis(limit: Int) = take(limit) + if (length > limit) "..." else ""
fun HtmlBlockTag.padding(height: Int) = div { style = "height: ${height}px;" }
val ApplicationCall.path get() = request.path()

fun HtmlBlockInlineTag.renderWithBBCode(text: String, s: LocalizedStrings) {
    BBCodeRenderer(this, s).render(text)
}

fun HtmlBlockTag.plusButton(url: String, alt: String) {
    a(href = url, classes = "b-img-a") {
        img(alt = alt, src = "/img/green-plus.png", classes = "b-plus")
    }
}

fun HtmlBlockTag.deleteButton(url: String, alt: String) {
    a(href = url, classes = "b-img-b") { img(alt = alt, src = "/img/x-red.png", classes = "b-x") }
}

fun HtmlBlockTag.formattedTextInput(s: LocalizedStrings) {
    textArea(rows = "8", classes = "fti") {
        name = "content"
        placeholder = s.typeSomething
    }
    br()
    padding(2)
    a(href = "/tools/help/bbcode", classes = "a1 forum-topic") { +s.formattedTextNotice }
}

fun HtmlInlineTag.renderWithNewlines(text: String, preserveAllFormatting: Boolean = false) {
    val split = text.split("\n")
    for ((index, line) in split.withIndex()) {
        +if (preserveAllFormatting) line.replace(" ", "\u00A0") else line
        if (index != split.lastIndex)
            br()
    }
}

fun HtmlBlockTag.pageNumbers(
    page: Int,
    pageCount: Int,
    call: ApplicationCall,
    s: LocalizedStrings
) {
    div(classes = "page-numbers") {
        // Show all page buttons if there are less than ten. If not, a navigator with first,
        // previous, next, last, and custom page selector buttons will be shown.
        if (pageCount < 10) {
            for (index in 0 until pageCount) {
                // [padding] ensures there is no extra padding on the last element.
                val padding = if (index < pageCount - 1) "a-page" else ""
                a(href = "${call.path}?p=$index", classes = "a2 $padding") {
                    +if (index == page) "(${(index + 1)})" else (index + 1).toString()
                }
            }
        } else {
            if (page > 0) {
                // First and previous page buttons.
                a(href = call.path, classes = "a2 a-page") { +s.first }
                a(href = "${call.path}?p=${page - 1}", classes = "a2 a-page") { +s.prev }
            }
            // Current page button/indicator.
            a(classes = "a2 a-page cursor-hand") {
                onClick = """
                    var page = parseInt(prompt("Enter the page to go to.", "${page + 1}"));
                    window.location.href = "${call.path}?p=" + (page - 1);
                """
                +"(${page + 1} ${s.of} $pageCount)"
            }
            if (page < pageCount - 1) {
                // Next and last page buttons.
                a(href = "${call.path}?p=${page + 1}", classes = "a2 a-page") { +s.next }
                val last = pageCount - 1
                a(href = "${call.path}?p=$last", classes = "a2") { +s.last }
            }
        }
    }
}
