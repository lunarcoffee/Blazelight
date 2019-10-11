package dev.lunarcoffee.blazelight.model.internal.threads

import dev.lunarcoffee.blazelight.model.internal.util.UniqueIDGenerator

class UserComment(override val contentRaw: String, override val authorId: Long) : Comment {
    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
