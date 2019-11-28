package dev.lunarcoffee.blazelight.model.internal.users

open class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray,
    override val imDataListId: Long,
    final override var settings: UserSettings
) : User {

    override var description: String? = null
    override var realName: String? = null

    override val commentIds = mutableListOf<Long>()
    override val threadIds = mutableListOf<Long>()

    override var creationTime = System.currentTimeMillis()
    final override val id = settings.userId

    override var isAdmin = false
}
