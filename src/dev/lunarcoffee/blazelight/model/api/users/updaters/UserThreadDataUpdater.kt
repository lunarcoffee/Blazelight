package dev.lunarcoffee.blazelight.model.api.users.updaters

import dev.lunarcoffee.blazelight.model.api.users.UserCache
import dev.lunarcoffee.blazelight.model.api.users.getUser
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.users.User
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

object UserThreadDataUpdater {
    suspend fun addThread(threadId: Long, userId: Long) {
        val newThreadIds = userId.getUser()!!.threadIds + threadId
        userId.updateDbUserThreadIds(newThreadIds)
        UserCache.cacheFromDB(userId)
    }

    suspend fun deleteThread(threadId: Long, userId: Long) {
        val newThreadIds = userId.getUser()!!.threadIds - threadId
        userId.updateDbUserThreadIds(newThreadIds)

        // Remove to clear old data, then reload data.
        UserCache.users.removeIf { it.id == userId }
        UserCache.cacheFromDB(userId)
    }

    private suspend fun Long.updateDbUserThreadIds(newThreadIds: List<Long>) {
        Database.userCol.updateOne(User::id eq this, setValue(User::threadIds, newThreadIds))
    }
}
