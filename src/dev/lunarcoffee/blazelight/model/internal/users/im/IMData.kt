package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface IMData<T : IMMessage> : Dateable, Identifiable {
    val messages: MutableList<T>

    val authorId: Long
    val recipientId: Long
}
