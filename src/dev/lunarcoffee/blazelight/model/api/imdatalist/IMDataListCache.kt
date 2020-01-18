package dev.lunarcoffee.blazelight.model.api.imdatalist

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import dev.lunarcoffee.blazelight.model.internal.std.DBCacheable
import dev.lunarcoffee.blazelight.model.internal.std.util.Cache
import dev.lunarcoffee.blazelight.model.internal.users.im.IUserIMDataList
import org.litote.kmongo.`in`
import org.litote.kmongo.eq

object IMDataListCache : DBCacheable<IUserIMDataList> {
    val imDataLists = Cache<IUserIMDataList>()

    override suspend fun cacheFromDB(id: Long): IUserIMDataList? {
        return Database.imDataListCol.findOne(Forum::id eq id)?.also { imDataLists += it }
    }

    override suspend fun cacheManyFromDB(ids: List<Long>): List<IUserIMDataList> {
        return Database
            .imDataListCol
            .find(IUserIMDataList::id `in` ids)
            .toList()
            .also { imDataLists += it }
    }
}
