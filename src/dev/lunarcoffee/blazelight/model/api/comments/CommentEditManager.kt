package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import dev.lunarcoffee.blazelight.model.internal.forums.UserComment
import org.litote.kmongo.eq

object CommentEditManager {
    suspend fun edit(comment: Comment): CommentEditResult {
        if (comment.contentRaw.length !in 1..10_000)
            return CommentEditResult.INVALID_CONTENT

        CommentCache.comments.removeIf { it.id == comment.id }
        CommentCache.comments += comment
        Database.commentCol.replaceOne(Comment::id eq comment.id, comment as UserComment)

        return CommentEditResult.SUCCESS
    }
}
