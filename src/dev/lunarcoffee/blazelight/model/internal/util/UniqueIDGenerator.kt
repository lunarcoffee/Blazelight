package dev.lunarcoffee.blazelight.model.internal.util

import dev.lunarcoffee.blazelight.model.internal.Database
import kotlinx.coroutines.*
import kotlin.random.Random

object UniqueIDGenerator : CoroutineScope by CoroutineScope(Dispatchers.IO) {
    private val generated = runBlocking { Database.idCol.find().first() ?: IdSetWrapper() }

    fun nextId(): Long {
        val id = Random(System.nanoTime()).nextLong(1, Long.MAX_VALUE)

        // This can theoretically cause a stack overflow, but in practice that should never happen.
        return if (id !in generated.set) {
            generated.set += id
            Database.idCol.run {
                runBlocking {
                    drop()
                    insertOne(generated)
                }
            }
            id
        } else {
            nextId()
        }
    }
}
