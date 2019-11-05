package dev.lunarcoffee.blazelight.model.api.categories

fun Long.getCategory() = CategoryCache.categories.find { this == it.id }
