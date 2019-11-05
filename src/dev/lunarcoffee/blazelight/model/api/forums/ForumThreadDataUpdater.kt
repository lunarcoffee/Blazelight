package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.threads.getThread
import dev.lunarcoffee.blazelight.model.api.users.updaters.UserThreadDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object ForumThreadDataUpdater {
    suspend fun addThread(threadId: Long, forumId: Long) {
        val newThreadIds = forumId.getForum()!!.threadIds + threadId
        forumId.updateDbThreadIds(newThreadIds)

        ForumCache.reloadFromDb(forumId)
        UserThreadDataUpdater.addThread(threadId, threadId.getThread()!!.authorId)
    }

    suspend fun deleteThread(threadId: Long, forumId: Long, authorId: Long) {
        val newThreadIds = forumId.getForum()!!.threadIds - threadId
        forumId.updateDbThreadIds(newThreadIds)

        ForumCache.reloadFromDb(forumId)
        UserThreadDataUpdater.deleteThread(threadId, authorId)
    }

    private suspend fun Long.updateDbThreadIds(newThreadIds: List<Long>) {
        Database.forumCol.updateOne(Forum::id eq this, setValue(Forum::threadIds, newThreadIds))
    }
}
