package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.shared.config.BL_CONFIG
import dev.lunarcoffee.blazelight.shared.language.Language
import java.time.ZoneId

class UserSettings(val zoneId: ZoneId, val language: Language, val userId: Long) {
    var theme = BL_CONFIG.defaultStyle

    // Accessibility settings.
    val buttonOutline = false
}
