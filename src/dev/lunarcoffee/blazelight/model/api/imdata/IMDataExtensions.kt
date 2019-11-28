package dev.lunarcoffee.blazelight.model.api.imdata

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.im.IMDataList
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq

fun Long.getIMDataList(): IMDataList? {
    return runBlocking { Database.imDataListCol.findOne(IMDataList::id eq this@getIMDataList) }
}
