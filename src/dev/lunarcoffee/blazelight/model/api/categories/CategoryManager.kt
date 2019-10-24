package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.*
import dev.lunarcoffee.blazelight.model.internal.users.User
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object CategoryManager {
    var categories = runBlocking { Database.categoryCol.find().toList() }

    suspend fun add(name: String, user: User): CategoryAddResult {
        if (!user.isAdmin)
            return CategoryAddResult.INSUFFICIENT_PERMISSIONS
        if (name.length !in 1..100)
            return CategoryAddResult.INVALID_NAME

        Database.categoryCol.insertOne(DefaultCategory(name))
        reloadDbChanges()

        return CategoryAddResult.SUCCESS
    }

    suspend fun addForum(forum: Forum) {
        val category = forum.categoryId.getCategory() ?: return
        val newForums = category.forumIds + forum.id

        Database.categoryCol.updateOne(
            Category::id eq category.id,
            setValue(Category::forumIds, newForums)
        )
        reloadDbChanges()
    }

    private suspend fun reloadDbChanges() {
        categories = Database.categoryCol.find().toList()
    }
}
