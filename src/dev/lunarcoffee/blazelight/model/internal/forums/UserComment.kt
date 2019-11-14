package dev.lunarcoffee.blazelight.model.internal.forums

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class UserComment(
    override val contentRaw: String,
    override val authorId: Long,
    override val threadId: Long
) : Comment {

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
