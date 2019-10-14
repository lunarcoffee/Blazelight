package dev.lunarcoffee.blazelight.std

import java.time.LocalDateTime
import java.time.ZoneId

object TimeZoneManager {
    // All UTC offset distinct [ZoneId]s.
    val timeZones = ZoneId
        .getAvailableZoneIds()
        .map { ZoneId.of(it) }
        .sortedBy { LocalDateTime.now().atZone(it).offset }
        .filter { "GMT" in it.id }
        .distinctBy { LocalDateTime.now().atZone(it).offset }

    fun toTimeZone(str: String) = timeZones[str.toInt()]!!
}
