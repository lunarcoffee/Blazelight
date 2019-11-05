package dev.lunarcoffee.blazelight.model.api.threads

import dev.lunarcoffee.blazelight.model.api.comments.CommentAddManager
import dev.lunarcoffee.blazelight.model.api.forums.ForumThreadDataUpdater
import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.UserComment
import dev.lunarcoffee.blazelight.model.internal.forums.UserThread
import dev.lunarcoffee.blazelight.model.internal.users.User

object ThreadAddManager {
    suspend fun add(title: String, content: String, forumId: Long, user: User): ThreadAddResult {
        if (title.length !in 1..300)
            return ThreadAddResult.INVALID_NAME
        if (content.length !in 1..10_000)
            return ThreadAddResult.INVALID_CONTENT

        val comment = UserComment(content, user.id)
        val thread = UserThread(title, user.id, forumId)

        ThreadCache.threads += thread
        Database.threadCol.insertOne(thread)
        CommentAddManager.add(comment, thread.id)

        ForumThreadDataUpdater.addThread(thread.id, forumId)
        return ThreadAddResult.SUCCESS
    }
}
