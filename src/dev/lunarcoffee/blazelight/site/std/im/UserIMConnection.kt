package dev.lunarcoffee.blazelight.site.std.im

import dev.lunarcoffee.blazelight.model.internal.users.User
import io.ktor.http.cio.websocket.*

class UserIMConnection(ws: WebSocketSession, private val user: User) : IMConnection {
    private val outChan = ws.outgoing
    private val inChan = ws.incoming

    override suspend fun send(message: String) = outChan.send(Frame.Text(message))

    // If [null] is returned, the connection has been closed.
    override suspend fun receive(): String? {
        val frame = inChan.receive()
        if (frame is Frame.Close)
            return null

        if (frame is Frame.Text)
            return frame.readText()
        return null
    }
}
