package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.api.threads.ThreadCommentDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import org.litote.kmongo.eq

object CommentDeleteManager {
    suspend fun delete(comment: Comment, threadId: Long) {
        CommentCache.comments.removeIf { it.id == comment.id }
        Database.commentCol.deleteOne(Comment::id eq comment.id)

        // This call will begin propagating the reloading of in-memory data.
        ThreadCommentDataUpdater.deleteComment(comment.id, threadId, comment.authorId)
    }
}
