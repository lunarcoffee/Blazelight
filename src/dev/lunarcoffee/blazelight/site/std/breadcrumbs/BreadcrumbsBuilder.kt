package dev.lunarcoffee.blazelight.site.std.breadcrumbs

class BreadcrumbsBuilder {
    val breadcrumbs = mutableListOf<BreadcrumbsEntry>()

    fun crumb(route: String, display: String) {
        breadcrumbs += BreadcrumbsEntry(route, display)
    }
}
