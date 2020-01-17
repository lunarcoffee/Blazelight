package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import kotlinx.coroutines.runBlocking

fun Long.getThread(): Thread? {
    return ThreadCache.threads.find { this == it.id }
        ?: runBlocking { ThreadCache.cacheFromDB(this@getThread) }
}

fun List<Long>.getThreads(): List<Thread> {
    val threads = ThreadCache.threads.filter { it.id in this }
    if (threads.size == size)
        return threads

    val remainingUncached = this - threads.map { it.id }
    val remaining = runBlocking { ThreadCache.cacheManyFromDB(remainingUncached) }

    return (threads + remaining).sortedByDescending { it.creationTime }
}
