package dev.lunarcoffee.blazelight.model.api.forums

fun Long.getForum() = ForumCache.forums.find { this == it.id }
