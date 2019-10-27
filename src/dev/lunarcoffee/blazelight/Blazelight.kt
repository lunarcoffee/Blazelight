package dev.lunarcoffee.blazelight

import dev.lunarcoffee.blazelight.shared.config.loadBlazelightConfig
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    loadBlazelightConfig(args.firstOrNull())
    EngineMain.main(args)
}
