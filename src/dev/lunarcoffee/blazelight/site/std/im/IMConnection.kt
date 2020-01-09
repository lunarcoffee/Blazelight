package dev.lunarcoffee.blazelight.site.std.im

interface IMConnection {
    suspend fun send(message: String)
    suspend fun receive(): String?
}
