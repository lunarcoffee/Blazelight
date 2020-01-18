package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class UserIMMessage(
    override val contentRaw: String,
    override val authorId: Long,
    override val recipientId: Long,
    override val dataId: Long
) : IMMessage {

    override val creationTime = System.currentTimeMillis()
    override val id: Long = UniqueIDGenerator.nextId()
}
