package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryManager
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.DefaultForum
import dev.lunarcoffee.blazelight.model.internal.users.User
import kotlinx.coroutines.runBlocking

object ForumManager {
    var forums = runBlocking { Database.forumCol.find().toList().toMutableSet() }

    suspend fun add(name: String, topic: String, categoryId: Long, user: User): ForumAddResult {
        if (!user.isAdmin)
            return ForumAddResult.INSUFFICIENT_PERMISSIONS
        if (name.length !in 1..100)
            return ForumAddResult.INVALID_NAME
        if (topic.length !in 1..300)
            return ForumAddResult.INVALID_TOPIC

        val forum = DefaultForum(name, topic, categoryId)
        CategoryManager.addForum(forum)
        Database.forumCol.insertOne(forum)
        forums.add(forum)

        return ForumAddResult.SUCCESS
    }
}
