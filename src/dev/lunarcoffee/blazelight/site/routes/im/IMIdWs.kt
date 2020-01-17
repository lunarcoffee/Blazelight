package dev.lunarcoffee.blazelight.site.routes.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.getIMDataList
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.site.std.im.IMServer
import dev.lunarcoffee.blazelight.site.std.im.UserIMConnection
import dev.lunarcoffee.blazelight.site.std.sessions.UserSession
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.websocket.webSocket

fun Route.imIdWs() = webSocket("/im/{dataId}/ws") {
    val user = call.sessions.get<UserSession>()!!.getUser()!!
    val connection = UserIMConnection(this, user)

    IMServer.connect(connection)

    val imDataList = user.imDataListId.getIMDataList()!!
    val dataId = call.parameters["dataId"]!!.toLongOrNull()
        ?: return@webSocket call.respond(HttpStatusCode.NotFound)

    val imData = imDataList.getByDataId(dataId)!!
    val recipientId = imData.recipientId

    IMServer.send(recipientId, "thing", imDataList)
}
