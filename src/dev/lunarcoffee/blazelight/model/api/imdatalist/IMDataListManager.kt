package dev.lunarcoffee.blazelight.model.api.imdatalist

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import dev.lunarcoffee.blazelight.model.internal.users.im.UserIMDataList
import org.litote.kmongo.eq

object IMDataListManager {
    suspend fun update(new: IUserIMDataList) {
        Database.imDataListCol.replaceOne(IUserIMDataList::id eq new.id, new as UserIMDataList)
    }
}
