package dev.lunarcoffee.blazelight.site.routes.im

import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.Route
import io.ktor.websocket.webSocket
import kotlinx.coroutines.selects.select

fun Route.imIdWs() = webSocket("/im/{dataId}/ws") {
    val dataId =
    try {
        while (true) {
            select<Unit> {
                incoming.onReceive {
                    val message = it as? Frame.Text ?: return@onReceive
                    val text = message.readText()
                }
            }
        }
    } finally {

    }
}
