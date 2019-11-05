package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.api.categories.CategoryCache
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq

object ForumCache {
    var forums = runBlocking { Database.forumCol.find().toList().toMutableSet() }

    suspend fun reloadFromDb(id: Long) {
        forums.removeIf { it.id == id }
        forums.add(Database.forumCol.findOne(Forum::id eq id)!!)

        // Propagate reloading of in-memory data.
        CategoryCache.reloadFromDb()
    }
}
