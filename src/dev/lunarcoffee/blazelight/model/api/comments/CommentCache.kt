package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import org.litote.kmongo.*

object CommentCache : DBCacheable<Comment> {
    val comments = Cache<Comment>()

    override suspend fun cacheFromDB(id: Long): Comment? {
        return Database.commentCol.findOne(Comment::id eq id)?.also { comments += it }
    }

    override suspend fun cacheManyFromDB(ids: List<Long>): List<Comment> {
        return Database
            .commentCol
            .find(Comment::id `in` ids)
            .toList()
            .also { comments += it }
    }
}
