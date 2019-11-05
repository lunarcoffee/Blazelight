package dev.lunarcoffee.blazelight.model.api.categories

import dev.lunarcoffee.blazelight.model.internal.Database
import kotlinx.coroutines.runBlocking

object CategoryCache {
    var categories = runBlocking { Database.categoryCol.find().toList() }

    suspend fun reloadFromDb() {
        categories = Database.categoryCol.find().toList()
    }
}
