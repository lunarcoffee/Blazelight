package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryForumDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import org.litote.kmongo.eq

object ForumDeleteManager {
    suspend fun delete(forum: Forum) {
        ForumCache.forums.removeIf { it.id == forum.id }
        Database.forumCol.deleteOne(Forum::id eq forum.id)

        CategoryForumDataUpdater.deleteForum(forum)
    }
}
