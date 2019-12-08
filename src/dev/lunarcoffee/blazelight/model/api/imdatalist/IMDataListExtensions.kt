package dev.lunarcoffee.blazelight.model.api.imdatalist

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.eq

fun Long.getIMDataList(): IUserIMDataList? {
    return runBlocking {
        Database.imDataListCol.findOne(IUserIMDataList::id eq this@getIMDataList)
    }
}
