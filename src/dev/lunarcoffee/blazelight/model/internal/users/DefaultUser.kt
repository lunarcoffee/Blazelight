package dev.lunarcoffee.blazelight.model.internal.users

class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray,
    override var settings: UserSettings
) : User {

    override val description: String? = null
    override val realName: String? = null

    override val commentIds = emptyList<Long>()
    override val threadIds = emptyList<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = settings.userId

    override var isAdmin = false
}
