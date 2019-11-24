package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface IMData : Dateable, Identifiable {
    val messages: List<IMMessage>

    val authorId: Long
    val recipientId: Long
}
