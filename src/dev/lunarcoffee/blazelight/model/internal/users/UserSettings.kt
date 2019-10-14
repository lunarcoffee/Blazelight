package dev.lunarcoffee.blazelight.model.internal.users

import dev.lunarcoffee.blazelight.std.Language
import java.time.ZoneId

class UserSettings(val zoneId: ZoneId, val language: Language, val userId: Long) {
    // Accessibility settings.
    val buttonOutline = false
}
