package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.forums.ForumThreadDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import org.litote.kmongo.eq

object ThreadDeleteManager {
    suspend fun delete(threadId: Long, forumId: Long) {
        val authorId = threadId.getThread()!!.authorId

        ThreadCache.threads.removeIf { it.id == threadId }
        Database.threadCol.deleteOne(Thread::id eq threadId)

        ForumThreadDataUpdater.deleteThread(threadId, forumId, authorId)
    }
}
