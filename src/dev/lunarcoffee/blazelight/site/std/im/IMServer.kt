package dev.lunarcoffee.blazelight.site.std.im

object IMServer {
    val conns = mutableListOf<UserIMConnection>()

    fun connect(conn: UserIMConnection) {
        conns += conn
    }
}
