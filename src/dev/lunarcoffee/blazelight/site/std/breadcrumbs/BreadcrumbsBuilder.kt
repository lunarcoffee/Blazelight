package dev.lunarcoffee.blazelight.site.std.breadcrumbs

class BreadcrumbsBuilder {
    val breadcrumbs = mutableListOf<BreadcrumbsEntry>()

    fun link(route: String, display: String) {
        breadcrumbs += BreadcrumbsEntry(route, display)
    }
}
