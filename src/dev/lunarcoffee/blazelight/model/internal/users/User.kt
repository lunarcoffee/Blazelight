package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.std.Dateable
import dev.lunarcoffee.blazelight.model.internal.std.Identifiable

interface User : Dateable, Identifiable {
    val username: String
    val description: String?

    // Private hidden or optionally publicly displayed information.
    val email: String
    val realName: String?
    val passwordHash: String
    val passwordSalt: ByteArray

    val commentIds: List<Long>
    val threadIds: List<Long>

    val settings: UserSettings
}
