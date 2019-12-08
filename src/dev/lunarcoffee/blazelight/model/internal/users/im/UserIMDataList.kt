package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator

class UserIMDataList(override val userId: Long) : IUserIMDataList {
    override val data = mutableListOf<UserIMData>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
