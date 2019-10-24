package dev.lunarcoffee.blazelight.model.internal.forums

import dev.lunarcoffee.blazelight.model.internal.util.UniqueIDGenerator

class UserThread(
    override val title: String,
    override val authorId: Long,
    override val forumId: Long
) : Thread {

    override val commentIds = mutableListOf<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
