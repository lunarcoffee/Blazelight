package dev.lunarcoffee.blazelight.model.internal.users

class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray,
    override var settings: UserSettings
) : User {

    // Constructor to be used for creation of "dummy" users for representing deleted users.
    constructor(user: User, name: String) : this(name, "", "", byteArrayOf(), user.settings) {
        description = user.description
        realName = name
        commentIds += user.commentIds
        threadIds += user.threadIds
        creationTime = user.creationTime
    }

    override var description: String? = null
    override var realName: String? = null

    override val commentIds = mutableListOf<Long>()
    override val threadIds = mutableListOf<Long>()

    override var creationTime = System.currentTimeMillis()
    override val id = settings.userId

    override var isAdmin = false
}
