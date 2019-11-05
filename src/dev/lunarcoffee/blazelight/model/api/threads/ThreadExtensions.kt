package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import kotlinx.coroutines.runBlocking

fun Long.getThread(): Thread? {
    return ThreadCache.threads.find { this == it.id }
        ?: runBlocking { ThreadCache.cacheFromDB(this@getThread) }
}
