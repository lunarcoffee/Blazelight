package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.forums.ForumCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Thread
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import org.litote.kmongo.*

object ThreadCache : DBCacheable<Thread> {
    val threads = Cache<Thread>()

    suspend fun reloadFromDb(id: Long) {
        threads.removeIf { it.id == id }
        threads.add(Database.threadCol.findOne(Thread::id eq id)!!)

        // Propagate reloading of in-memory data.
        ForumCache.reloadFromDb(id.getThread()!!.forumId)
    }

    override suspend fun cacheFromDB(id: Long): Thread? {
        return Database.threadCol.findOne(Thread::id eq id)?.also { threads += it }
    }

    override suspend fun cacheManyFromDB(ids: List<Long>): List<Thread> {
        return Database
            .threadCol
            .find(Thread::id `in` ids)
            .sort(orderBy(Thread::creationTime))
            .toList()
            .also { threads += it }
    }
}
