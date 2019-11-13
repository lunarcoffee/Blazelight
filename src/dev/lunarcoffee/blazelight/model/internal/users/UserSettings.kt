package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.Language
import java.time.ZoneId

data class UserSettings(
    val userId: Long,
    val zoneId: ZoneId,
    val language: Language,
    var theme: String = BL_CONFIG.defaultStyle,
    val buttonOutline: Boolean = false
)
