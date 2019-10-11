package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.util.UniqueIDGenerator

class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray
) : User {

    override val description: String? = null
    override val realName: String? = null

    override val commentIds = emptyList<Long>()
    override val threadIds = emptyList<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()
}
