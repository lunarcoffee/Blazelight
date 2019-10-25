package dev.lunarcoffee.blazelight.site.std.breadcrumbs

import io.ktor.application.ApplicationCall
import io.ktor.request.uri

class BreadcrumbsBuilder {
    val breadcrumbs = mutableListOf<BreadcrumbsEntry>()

    fun crumb(route: String, display: String) {
        breadcrumbs += BreadcrumbsEntry(route, display)
    }

    fun thisCrumb(call: ApplicationCall, display: String) = crumb(call.request.uri, display)
}
