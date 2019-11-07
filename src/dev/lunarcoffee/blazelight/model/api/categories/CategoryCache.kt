package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.DefaultCategory
import kotlinx.coroutines.runBlocking

object CategoryCache {
    var categories = runBlocking { reloadFromDb() }

    suspend fun reloadFromDb(): MutableList<DefaultCategory> {
        return Database.categoryCol.find().toList().toMutableList().also { categories = it }
    }
}
