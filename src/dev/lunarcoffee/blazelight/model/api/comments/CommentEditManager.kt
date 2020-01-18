package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import dev.lunarcoffee.blazelight.model.internal.forums.UserComment
import org.litote.kmongo.eq

object CommentEditManager {
    suspend fun edit(commentId: Long, content: String): CommentEditResult {
        if (content.length !in 1..10_000)
            return CommentEditResult.INVALID_CONTENT

        val comment = commentId.getComment()!!.apply { contentRaw = content }
        CommentCache.comments.run {
            removeIf { it.id == commentId }
            this += comment
        }
        Database.commentCol.replaceOne(Comment::id eq commentId, comment as UserComment)

        return CommentEditResult.SUCCESS
    }
}
