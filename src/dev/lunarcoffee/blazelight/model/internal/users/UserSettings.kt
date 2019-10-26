package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.shared.BlazelightConfig
import dev.lunarcoffee.blazelight.shared.Language
import java.time.ZoneId

class UserSettings(val zoneId: ZoneId, val language: Language, val userId: Long) {
    val theme = BlazelightConfig.defaultStyle

    // Accessibility settings.
    val buttonOutline = false
}
