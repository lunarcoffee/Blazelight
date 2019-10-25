package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryManager
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.DefaultForum
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import dev.lunarcoffee.blazelight.model.internal.users.User
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object ForumManager {
    var forums = runBlocking { Database.forumCol.find().toList().toMutableSet() }

    suspend fun add(name: String, topic: String, categoryId: Long, user: User): ForumAddResult {
        if (!user.isAdmin)
            return ForumAddResult.INSUFFICIENT_PERMISSIONS
        if (name.length !in 1..100)
            return ForumAddResult.INVALID_NAME
        if (topic.length !in 1..1_000)
            return ForumAddResult.INVALID_TOPIC

        val forum = DefaultForum(name, topic, categoryId)
        CategoryManager.addForum(forum)
        Database.forumCol.insertOne(forum)
        forums.add(forum)

        return ForumAddResult.SUCCESS
    }

    suspend fun addThread(threadId: Long, forumId: Long) {
        val newThreadIds = forumId.getForum()!!.threadIds + threadId
        Database.forumCol.updateOne(
            Forum::id eq forumId,
            setValue(Forum::threadIds, newThreadIds)
        )
        reloadFromDb(forumId)
    }

    suspend fun reloadFromDb(id: Long) {
        forums.removeIf { it.id == id }
        forums.add(Database.forumCol.findOne(Forum::id eq id)!!)

        // Propagate reloading of in-memory data.
        CategoryManager.reloadFromDb()
    }
}
