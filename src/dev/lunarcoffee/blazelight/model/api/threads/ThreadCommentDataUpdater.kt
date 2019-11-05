package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.comments.getComment
import dev.lunarcoffee.blazelight.model.api.users.updaters.UserCommentDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object ThreadCommentDataUpdater {
    suspend fun addComment(commentId: Long, threadId: Long) {
        val newCommentIds = threadId.getThread()!!.commentIds + commentId
        threadId.updateDbCommentIds(newCommentIds)

        ThreadCache.reloadFromDb(threadId)
        UserCommentDataUpdater.addComment(commentId, commentId.getComment()!!.authorId)
    }

    suspend fun deleteComment(commentId: Long, threadId: Long, authorId: Long) {
        val newCommentIds = threadId.getThread()!!.commentIds - commentId
        threadId.updateDbCommentIds(newCommentIds)

        ThreadCache.reloadFromDb(threadId)
        UserCommentDataUpdater.deleteComment(commentId, authorId)
    }

    private suspend fun Long.updateDbCommentIds(newCommentIds: List<Long>) {
        Database.threadCol.updateOne(
            Thread::id eq this,
            setValue(Thread::commentIds, newCommentIds)
        )
    }
}
