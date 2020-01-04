package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import kotlinx.coroutines.runBlocking

fun Long.getComment(): Comment? {
    return CommentCache.comments.find { this == it.id }
        ?: runBlocking { CommentCache.cacheFromDB(this@getComment) }
}

fun List<Long>.getComments(): List<Comment> {
    val comments = CommentCache.comments.filter { it.id in this }
    val remainingUncached = this - comments.map { it.id }
    val remaining = runBlocking { CommentCache.cacheManyFromDB(remainingUncached) }

    return comments + remaining
}
