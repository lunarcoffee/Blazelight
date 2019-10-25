package dev.lunarcoffee.blazelight.model.internal.forums

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class DefaultCategory(override val name: String) : Category {
    override val forumIds = mutableListOf<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
