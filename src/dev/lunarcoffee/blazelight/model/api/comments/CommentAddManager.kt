package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.api.threads.ThreadCommentDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import dev.lunarcoffee.blazelight.model.internal.forums.UserComment

object CommentAddManager {
    suspend fun add(comment: Comment, threadId: Long): CommentAddResult {
        if (comment.contentRaw.length !in 1..10_000)
            return CommentAddResult.INVALID_CONTENT

        CommentCache.comments += comment
        Database.commentCol.insertOne(comment as UserComment)

        // This call will begin propagating the reloading of in-memory data.
        ThreadCommentDataUpdater.addComment(comment.id, threadId)
        return CommentAddResult.SUCCESS
    }
}
