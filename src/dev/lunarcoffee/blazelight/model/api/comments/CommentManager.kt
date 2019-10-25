package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.api.threads.ThreadManager
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import dev.lunarcoffee.blazelight.model.internal.forums.UserComment
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import org.litote.kmongo.eq

object CommentManager : DBCacheable<Comment> {
    val comments = Cache<Comment>()

    suspend fun add(comment: Comment, threadId: Long): CommentAddResult {
        if (comment.contentRaw.length !in 30..10_000)
            return CommentAddResult.INVALID_CONTENT

        comments += comment
        Database.commentCol.insertOne(comment as UserComment)

        // This call will begin propagating the reloading of in-memory data.
        ThreadManager.addComment(comment.id, threadId)
        return CommentAddResult.SUCCESS
    }

    override suspend fun cacheFromDB(id: Long): Comment? {
        return Database.commentCol.findOne(Comment::id eq id)?.also { comments += it }
    }
}
