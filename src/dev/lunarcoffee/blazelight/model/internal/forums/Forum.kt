package dev.lunarcoffee.blazelight.model.internal.forums

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface Forum : Dateable, Identifiable {
    val name: String
    val description: String

    val threadIds: MutableList<Long>
    val categoryId: Long
}
