package dev.lunarcoffee.blazelight.site.std.im

import dev.lunarcoffee.blazelight.model.internal.users.User
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException

class UserIMConnection(ws: WebSocketSession, user: User) : IMConnection {
    private val outChan = ws.outgoing
    private val inChan = ws.incoming

    override val userId = user.id

    // Everything sent will be displayed as a message from the other user on the client page.
    override suspend fun send(message: String) {
        runCatching { outChan.send(Frame.Text(message)) }
    }

    // If [null] is returned, the connection should be closed.
    override suspend fun receive(): String? {
        val frame = try {
            inChan.receive()
        } catch (e: ClosedReceiveChannelException) {
            return null
        }

        if (frame is Frame.Text)
            return frame.readText()
        return null
    }
}
