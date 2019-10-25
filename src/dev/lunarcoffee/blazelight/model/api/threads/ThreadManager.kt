package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.*
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import org.litote.kmongo.eq

object ThreadManager : DBCacheable<Thread> {
    val threads = Cache<Thread>()

    fun add(title: String, comment: Comment, forumId: Long) {
        val thread = UserThread(title, comment.authorId, forumId)
        threads += thread
        // TODO: Formalize, add to DB.
    }

    override suspend fun cacheFromDB(id: Long): Thread? {
        return Database.threadCol.findOne(Thread::id eq id)?.also { threads += it }
    }
}
