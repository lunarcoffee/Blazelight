package dev.lunarcoffee.blazelight.model.internal.threads

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface Thread : Dateable,
    Identifiable {
    val title: String

    val authorId: Long
    val commentIds: List<Long>
    val categoryId: Long
}
