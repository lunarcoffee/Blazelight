package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.comments.CommentManager
import dev.lunarcoffee.blazelight.model.api.forums.ForumManager
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.*
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object ThreadManager : DBCacheable<Thread> {
    val threads = Cache<Thread>()

    suspend fun add(title: String, content: String, forumId: Long, user: User): ThreadAddResult {
        if (title.length !in 1..300)
            return ThreadAddResult.INVALID_NAME
        if (content.length !in 30..10_000)
            return ThreadAddResult.INVALID_CONTENT

        val comment = UserComment(content, user.id)
        val thread = UserThread(title, user.id, forumId)

        threads += thread
        Database.threadCol.insertOne(thread)
        CommentManager.add(comment, thread.id)

        ForumManager.addThread(thread.id, forumId)
        return ThreadAddResult.SUCCESS
    }

    suspend fun addComment(commentId: Long, threadId: Long) {
        val newCommentIds = threadId.getThread()!!.commentIds + commentId
        Database.threadCol.updateOne(
            Thread::id eq threadId,
            setValue(Thread::commentIds, newCommentIds)
        )
        reloadFromDb(threadId)
    }

    override suspend fun cacheFromDB(id: Long): Thread? {
        return Database.threadCol.findOne(Thread::id eq id)?.also { threads += it }
    }

    private suspend fun reloadFromDb(id: Long) {
        threads.removeIf { it.id == id }
        threads.add(Database.threadCol.findOne(Thread::id eq id)!!)

        // Propagate reloading of in-memory data.
        ForumManager.reloadFromDb(id.getThread()!!.forumId)
    }
}
