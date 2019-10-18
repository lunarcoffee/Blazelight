package dev.lunarcoffee.blazelight.model.api.categories

fun Long.getCategory() = CategoryManager.categories.find { this == it.id }!!
