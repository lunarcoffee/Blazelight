package dev.lunarcoffee.blazelight.model.internal

import dev.lunarcoffee.blazelight.model.internal.forums.*
import dev.lunarcoffee.blazelight.model.internal.users.DefaultUser
import dev.lunarcoffee.blazelight.model.internal.util.IdSetWrapper
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    private val db = KMongo.createClient().coroutine.getDatabase("Blazelight")

    val userCol = db.getCollection<DefaultUser>("BL_User0")
    val categoryCol = db.getCollection<DefaultCategory>("BL_Category0")
    val threadCol = db.getCollection<UserThread>("BL_Thread0")
    val commentCol = db.getCollection<UserComment>("BL_Comment0")

    val idCol = db.getCollection<IdSetWrapper>("BL_OldID0")
}
