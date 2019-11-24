package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class UserIMData(override val authorId: Long, override val recipientId: Long) : IMData {
    override val messages = mutableListOf<IMMessage>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
