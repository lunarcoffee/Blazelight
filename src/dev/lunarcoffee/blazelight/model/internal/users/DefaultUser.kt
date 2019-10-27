package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.model.internal.std.util.UniqueIDGenerator
import dev.lunarcoffee.blazelight.shared.language.Language
import java.time.ZoneId

class DefaultUser(
    override val username: String,
    override val email: String,
    override val passwordHash: String,
    override val passwordSalt: ByteArray,
    zoneId: ZoneId = ZoneId.systemDefault(),
    language: Language = Language.ENGLISH
) : User {

    override val description: String? = null
    override val realName: String? = null

    override val commentIds = emptyList<Long>()
    override val threadIds = emptyList<Long>()

    override val creationTime = System.currentTimeMillis()
    override val id = UniqueIDGenerator.nextId()

    override val settings = UserSettings(zoneId, language, id)
    override var isAdmin = false
}
