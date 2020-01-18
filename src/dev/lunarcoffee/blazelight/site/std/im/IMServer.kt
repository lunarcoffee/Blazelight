package dev.lunarcoffee.blazelight.site.std.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.IMDataListManager
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import dev.lunarcoffee.blazelight.model.internal.users.im.UserIMMessage

object IMServer {
    private val conns = mutableListOf<UserIMConnection>()

    fun connect(conn: UserIMConnection) = conns.add(conn)
    fun disconnect(conn: UserIMConnection) = conns.remove(conn)

    suspend fun send(
        recipientId: Long,
        message: String,
        authorDataList: IUserIMDataList,
        recipientDataList: IUserIMDataList,
        authorDataId: Long
    ) {
        val authorConn = conns.find { it.userId == authorDataList.userId }
        val recipientConn = conns.find { it.userId == recipientId }

        authorConn?.send("a$message")
        recipientConn?.send("r$message")

        // Cache messages; actual DB transaction happens upon disconnection.
        val imMessage = UserIMMessage(message, authorDataList.userId, recipientId, authorDataId)

        authorDataList.addByDataId(authorDataId, imMessage)
        IMDataListManager.updateCache(authorDataList)

        val recipientDataId = recipientDataList
            .data
            .find { it.recipientId == authorDataList.userId }!!
            .id
        recipientDataList.addByDataId(recipientDataId, imMessage)
        IMDataListManager.updateCache(recipientDataList)
    }
}
