package dev.lunarcoffee.blazelight.model.api.users.updaters

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object UserCommentDataUpdater {
    suspend fun addComment(commentId: Long, userId: Long) {
        val newCommentIds = userId.getUser()!!.commentIds + commentId
        userId.updateDbUserCommentIds(newCommentIds)
        UserCache.cacheFromDB(userId)
    }

    suspend fun deleteComment(commentId: Long, userId: Long) {
        val newCommentIds = userId.getUser()!!.commentIds - commentId
        userId.updateDbUserCommentIds(newCommentIds)

        // Remove to clear old data, then reload data.
        UserCache.users.removeIf { it.id == userId }
        UserCache.cacheFromDB(userId)
    }

    private suspend fun Long.updateDbUserCommentIds(newCommentIds: List<Long>) {
        Database.userCol.updateOne(User::id eq this, setValue(User::commentIds, newCommentIds))
    }
}
