package dev.lunarcoffee.blazelight.site.routes.im

import io.ktor.routing.Route
import io.ktor.websocket.webSocket

fun Route.imIdWs() = webSocket("/im/{dataId}/ws") {

}
