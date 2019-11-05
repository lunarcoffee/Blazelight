package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryForumDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.DefaultForum
import dev.lunarcoffee.blazelight.model.internal.users.User

object ForumAddManager {
    suspend fun add(name: String, topic: String, categoryId: Long, user: User): ForumAddResult {
        if (!user.isAdmin)
            return ForumAddResult.INSUFFICIENT_PERMISSIONS
        if (name.length !in 1..100)
            return ForumAddResult.INVALID_NAME
        if (topic.length !in 1..1_000)
            return ForumAddResult.INVALID_TOPIC

        val forum = DefaultForum(name, topic, categoryId)
        CategoryForumDataUpdater.addForum(forum)
        Database.forumCol.insertOne(forum)
        ForumCache.forums.add(forum)

        return ForumAddResult.SUCCESS
    }
}
