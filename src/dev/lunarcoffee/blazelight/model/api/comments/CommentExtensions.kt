package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import kotlinx.coroutines.runBlocking

fun Long.getComment(): Comment? {
    return CommentCache.comments.find { this == it.id }
        ?: runBlocking { CommentCache.cacheFromDB(this@getComment) }
}
