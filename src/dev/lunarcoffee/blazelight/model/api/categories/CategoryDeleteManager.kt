package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.api.forums.ForumDeleteManager
import dev.lunarcoffee.blazelight.model.api.forums.getForum
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Category
import org.litote.kmongo.eq

object CategoryDeleteManager {
    suspend fun delete(category: Category) {
        for (forumId in category.forumIds)
            ForumDeleteManager.delete(forumId.getForum()!!)

        CategoryCache.categories.removeIf { it.id == category.id }
        Database.categoryCol.deleteOne(Category::id eq category.id)
    }
}
