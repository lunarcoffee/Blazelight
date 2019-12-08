package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class UserIMData(
    override val authorId: Long,
    override val recipientId: Long
) : IMData<UserIMMessage> {

    override val messages = mutableListOf<UserIMMessage>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
