package dev.lunarcoffee.blazelight.model.internal.threads

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface Comment : Dateable,
    Identifiable {
    val contentRaw: String
    val authorId: Long
}
