package dev.lunarcoffee.blazelight.site.std.breadcrumbs

import kotlinx.html.*

fun HtmlBlockTag.breadcrumbs(breadcrumbsBuilder: BreadcrumbsBuilder.() -> Unit) {
    val breadcrumbs = BreadcrumbsBuilder().apply(breadcrumbsBuilder)
    p {
        a(href = "/") { img(src = "/img/home.ico", alt = "Home", classes = "bc-home") }
        for ((route, display) in breadcrumbs.breadcrumbs) {
            img(src = "/img/arrow-right.svg", alt = "Separator", classes = "bc-arrow")
            a(href = route, classes = "a1 bc-a title") { +display }
        }
    }
}
