package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

// [IMDataList]s store a list of a [IMData] that store message history.
interface IMDataList : Dateable, Identifiable {
    val data: List<IMData>
    val userId: Long
}
