package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.users.im.IMData

open class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray,
    final override var settings: UserSettings
) : User {

    override var description: String? = null
    override var realName: String? = null

    override val commentIds = mutableListOf<Long>()
    override val threadIds = mutableListOf<Long>()

    override var creationTime = System.currentTimeMillis()
    final override val id = settings.userId

    override val imData = emptyList<IMData>()

    override var isAdmin = false
}
