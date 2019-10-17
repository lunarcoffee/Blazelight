package dev.lunarcoffee.blazelight.model.api.forums

import dev.lunarcoffee.blazelight.model.internal.Database
import dev.lunarcoffee.blazelight.model.internal.forums.Forum
import org.litote.kmongo.eq

suspend fun Long.getForum() = Database.forumCol.findOne(Forum::id eq this)!!
