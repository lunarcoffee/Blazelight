package dev.lunarcoffee.blazelight.model.internal.users.im

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

typealias IUserIMDataList = IMDataList<UserIMData, UserIMMessage>

// [IMDataList]s store a list of a [IMData] that store message history.
interface IMDataList<T : IMData<V>, V : IMMessage> : Dateable, Identifiable {
    val data: MutableList<T>
    val userId: Long

    fun getByDataId(dataId: Long): T?
    fun addByDataId(dataId: Long, message: V)
}
