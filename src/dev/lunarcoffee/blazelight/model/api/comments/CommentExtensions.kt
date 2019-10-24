package dev.lunarcoffee.blazelight.model.api.comments

import dev.lunarcoffee.blazelight.model.internal.forums.Comment
import kotlinx.coroutines.runBlocking

fun Long.getComment(): Comment? {
    return CommentManager.comments.find { this == it.id }
        ?: runBlocking { CommentManager.cacheFromDB(this@getComment) }
}
