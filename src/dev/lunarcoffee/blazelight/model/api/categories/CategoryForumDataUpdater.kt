package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Category
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object CategoryForumDataUpdater {
    suspend fun addForum(forum: Forum) {
        val category = forum.categoryId.getCategory() ?: return
        val newForums = category.forumIds + forum.id

        forum.id.updateDbForumIds(newForums)
        CategoryCache.reloadFromDb()
    }

    suspend fun deleteForum(forum: Forum) {
        val category = forum.categoryId.getCategory() ?: return
        val newForums = category.forumIds - forum.id

        forum.id.updateDbForumIds(newForums)
        CategoryCache.reloadFromDb()
    }

    suspend fun Long.updateDbForumIds(newForums: List<Long>) {
        Database.categoryCol.updateOne(
            Category::id eq this,
            setValue(Category::forumIds, newForums)
        )
    }
}
