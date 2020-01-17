package dev.lunarcoffee.blazelight.site.std.im

interface IMConnection {
    val userId: Long

    suspend fun send(message: String)
    suspend fun receive(): String?
}
