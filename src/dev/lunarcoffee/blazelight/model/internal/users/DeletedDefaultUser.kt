package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.users.im.UserIMDataList

class DeletedDefaultUser(user: User, deleted: String) : DefaultUser(
    deleted,
    deleted,
    deleted,
    byteArrayOf(),
    UserIMDataList(user.settings.userId), // This effectively clears IM data.
    user.settings
) {
    override var description: String? = deleted
    override var realName: String? = deleted

    override val commentIds = user.commentIds.toMutableList()
    override val threadIds = user.threadIds.toMutableList()

    override var creationTime = user.creationTime
}
