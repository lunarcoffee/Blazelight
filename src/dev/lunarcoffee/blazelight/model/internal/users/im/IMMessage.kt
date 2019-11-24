package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface IMMessage : Dateable, Identifiable {
    val contentRaw: String

    val authorId: String
    val recipientId: String

    val dataId: String
}
