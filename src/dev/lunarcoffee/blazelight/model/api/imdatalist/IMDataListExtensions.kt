package dev.lunarcoffee.blazelight.model.api.imdatalist

import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import kotlinx.coroutines.runBlocking

fun Long.getIMDataList(): IUserIMDataList? {
    return IMDataListCache.imDataLists.find { this == it.id }
        ?: runBlocking { IMDataListCache.cacheFromDB(this@getIMDataList) }
}
