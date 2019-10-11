package dev.lunarcoffee.blazelight.model.internal

import dev.lunarcoffee.blazelight.model.internal.threads.Category
import dev.lunarcoffee.blazelight.model.internal.threads.Comment
import dev.lunarcoffee.blazelight.model.internal.users.User
import dev.lunarcoffee.blazelight.model.internal.util.IdSetWrapper
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    private val db = KMongo.createClient().coroutine.getDatabase("Blazelight")

    val userCol = db.getCollection<User>("BL_User0")
    val categoryCol = db.getCollection<Category>("BL_Category0")
    val threadCol = db.getCollection<Thread>("BL_Thread0")
    val commentCol = db.getCollection<Comment>("BL_Comment0")

    val idCol = db.getCollection<IdSetWrapper>("BL_OldID0")
}
