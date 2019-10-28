package dev.lunarcoffee.blazelight.site.std

import io.ktor.application.ApplicationCall
import io.ktor.request.path
import kotlinx.html.*

fun HtmlBlockTag.padding(height: Int) = div { style = "height: ${height}px;" }

fun HtmlBlockTag.pageNumbers(page: Int, pageCount: Int, call: ApplicationCall) {
    div(classes = "page-numbers") {
        // Show all page buttons if there are less than ten. If not, a navigator with first,
        // previous, next, last, and custom page selector buttons will be shown.
        if (pageCount < 10) {
            for (index in 0 until pageCount) {
                a(href = "${call.request.path()}?p=$index", classes = "a2 a-page") {
                    +if (index == page) "(${(index + 1)})" else (index + 1).toString()
                }
            }
        } else {
            if (page > 0) {
                // First and previous page buttons.
                a(href = call.request.path(), classes = "a2 a-page") { +"First" }
                a(href = "${call.request.path()}?p=${page - 1}", classes = "a2 a-page") {
                    +"Prev"
                }
            }
            // Current page button/indicator.
            a(classes = "a2 a-page cursor-hand") {
                onClick = """
                var page = parseInt(prompt("Enter the page to go to.", "${page + 1}"));
                window.location.href = "${call.request.path()}?p=" + (page - 1);
            """
                +"(${page + 1} of $pageCount)"
            }
            if (page < pageCount - 1) {
                // Next and last page buttons.
                a(href = "${call.request.path()}?p=${page + 1}", classes = "a2 a-page") {
                    +"Next"
                }
                val last = pageCount - 1
                a(href = "${call.request.path()}?p=$last", classes = "a2 a-page") {
                    +"Last"
                }
            }
        }
    }
}

fun String.textOrEllipsis(limit: Int) = take(limit) + if (length > limit) "..." else ""
