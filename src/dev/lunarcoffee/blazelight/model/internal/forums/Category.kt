package dev.lunarcoffee.blazelight.model.internal.forums

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface Category : Dateable, Identifiable {
    val name: String
    val forumIds: MutableList<Long>
}
