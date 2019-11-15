package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.comments.CommentEditManager
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import dev.lunarcoffee.blazelight.model.internal.forums.UserThread
import org.litote.kmongo.eq

object ThreadEditManager {
    suspend fun edit(threadId: Long, title: String, content: String): ThreadEditResult {
        if (title.length !in 1..300)
            return ThreadEditResult.INVALID_NAME
        if (content.length !in 1..10_000)
            return ThreadEditResult.INVALID_CONTENT

        val thread = threadId.getThread()!!.apply { this@apply.title = title }
        CommentEditManager.edit(thread.firstPost!!.id, content)

        ThreadCache.threads.removeIf { it.id == thread.id }
        ThreadCache.threads += thread
        Database.threadCol.replaceOne(Thread::id eq thread.id, thread as UserThread)

        return ThreadEditResult.SUCCESS
    }
}
