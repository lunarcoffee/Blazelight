package dev.lunarcoffee.blazelight

import dev.lunarcoffee.blazelight.shared.BlazelightConfig
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    BlazelightConfig.load("")
    EngineMain.main(args)
}
