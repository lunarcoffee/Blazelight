package dev.lunarcoffee.blazelight.model.internal.threads

import dev.lunarcoffee.blazelight.model.internal.util.UniqueIDGenerator

class UserThread(
    override val title: String,
    override val authorId: Long,
    override val categoryId: Long
) : Thread {

    override val commentIds = emptyList<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
