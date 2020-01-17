package dev.lunarcoffee.blazelight.site.std.im

import dev.lunarcoffee.blazelight.model.api.imdatalist.getIMDataList
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList

object IMServer {
    private val conns = mutableListOf<UserIMConnection>()

    fun connect(conn: UserIMConnection) {
        conns += conn
    }

    fun disconnect(conn: UserIMConnection) {
        conns -= conn
    }

    suspend fun send(userId: Long, message: String, imDataList: IUserIMDataList) {
        val recipientConn = conns.find { it.userId == userId }
        recipientConn?.send(message)

        val recipient = userId.getUser()!!
        val recipientDataList = recipient.imDataListId.getIMDataList()!!
    }
}
