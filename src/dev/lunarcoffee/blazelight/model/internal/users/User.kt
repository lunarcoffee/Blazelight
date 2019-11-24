package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable
import dev.lunarcoffee.blazelight.model.internal.users.im.IMData

interface User : Dateable, Identifiable {
    val username: String
    var description: String?

    // Private and hidden (or optionally publicly displayed) information.
    val email: String
    var realName: String?
    val passwordHash: String
    val passwordSalt: ByteArray

    val commentIds: List<Long>
    val threadIds: List<Long>

    var settings: UserSettings
    val imData: List<IMData> // TODO: should this directly store each or hold IDs
    val isAdmin: Boolean
}
