package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.DefaultCategory
import dev.lunarcoffee.blazelight.model.internal.users.User

object CategoryManager {
    suspend fun getAll() = Database.categoryCol.find().toList()

    suspend fun add(name: String, user: User): CategoryAddResult {
        if (!user.isAdmin)
            return CategoryAddResult.INSUFFICIENT_PERMISSIONS
        if (name.length !in 1..100)
            return CategoryAddResult.INVALID_NAME

        Database.categoryCol.insertOne(DefaultCategory(name))
        return CategoryAddResult.SUCCESS
    }
}
