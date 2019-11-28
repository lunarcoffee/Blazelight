package dev.lunarcoffee.blazelight.model.internal.users

class DeletedDefaultUser(user: User, deleted: String) : DefaultUser(
    deleted,
    deleted,
    deleted,
    byteArrayOf(),
    user.imDataListId,
    user.settings
) {
    override var description: String? = deleted
    override var realName: String? = deleted

    override val commentIds = user.commentIds.toMutableList()
    override val threadIds = user.threadIds.toMutableList()

    override var creationTime = user.creationTime
}
