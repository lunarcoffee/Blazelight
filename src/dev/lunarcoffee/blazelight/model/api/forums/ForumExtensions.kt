package dev.lunarcoffee.blazelight.model.api.forums

fun Long.getForum() = ForumManager.forums.find { this == it.id }!!
