package dev.lunarcoffee.blazelight.site.std.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.IMDataListManager
import dev.lunarcoffee.blazelight.model.api.imdatalist.getIMDataList
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import dev.lunarcoffee.blazelight.model.internal.users.im.UserIMMessage

object IMServer {
    private val conns = mutableListOf<UserIMConnection>()

    fun connect(conn: UserIMConnection) = conns.add(conn)
    fun disconnect(conn: UserIMConnection) = conns.remove(conn)

    suspend fun send(userId: Long, message: String, imDataList: IUserIMDataList, dataId: Long) {
        val authorConn = conns.find { it.userId == imDataList.userId }
        val recipientConn = conns.find { it.userId == userId }

        // Send messages.
        authorConn?.send("a$message")
        recipientConn?.send("r$message")

        // Update persistent message store for author.
        imDataList.addByDataId(dataId, UserIMMessage(message, imDataList.userId, userId, dataId))
        IMDataListManager.update(imDataList)

        val recipient = userId.getUser()!!
        val recipientDataList = recipient.imDataListId.getIMDataList()!!
        val recipientDataId = recipientDataList.data.find { it.authorId == userId }!!.id

        // Update persistent message store for recipient.
        recipientDataList.addByDataId(
            recipientDataId,
            UserIMMessage(message, imDataList.userId, userId, recipientDataId)
        )
        IMDataListManager.update(recipientDataList)
    }
}
