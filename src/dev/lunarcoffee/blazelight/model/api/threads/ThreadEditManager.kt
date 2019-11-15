package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.comments.CommentEditManager
import dev.lunarcoffee.blazelight.model.api.comments.CommentEditResult
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.*
import org.litote.kmongo.eq

object ThreadEditManager {
    suspend fun edit(thread: Thread, firstPost: Comment): ThreadEditResult {
        if (thread.title.length !in 1..300)
            return ThreadEditResult.INVALID_NAME
        if (CommentEditManager.edit(firstPost) != CommentEditResult.SUCCESS)
            return ThreadEditResult.INVALID_CONTENT

        ThreadCache.threads.removeIf { it.id == thread.id }
        ThreadCache.threads += thread
        Database.threadCol.replaceOne(Thread::id eq thread.id, thread as UserThread)

        return ThreadEditResult.SUCCESS
    }
}
