package dev.lunarcoffee.blazelight.model.internal.threads

import dev.lunarcoffee.blazelight.model.internal.util.UniqueIDGenerator

class DefaultCategory(override val name: String) : Category {
    override val threadIds = emptyList<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
