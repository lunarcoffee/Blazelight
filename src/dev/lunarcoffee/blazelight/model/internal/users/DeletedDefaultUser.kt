package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.users.im.IMData

class DeletedDefaultUser(user: User, deleted: String) : DefaultUser(
    deleted,
    deleted,
    deleted,
    byteArrayOf(),
    user.settings
) {
    override var description: String? = deleted
    override var realName: String? = deleted

    override val commentIds = user.commentIds.toMutableList()
    override val threadIds = user.threadIds.toMutableList()

    // Effectively clears IM data.
    override val imData = emptyList<IMData>()

    override var creationTime = user.creationTime
}
