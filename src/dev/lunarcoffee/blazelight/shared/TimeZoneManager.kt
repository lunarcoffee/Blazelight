package dev.lunarcoffee.blazelight.shared

import java.time.ZoneId

object TimeZoneManager {
    private val excludedIdPrefixes = setOf("Etc/", "SystemV/", "Universal")

    // All UTC offset distinct [ZoneId]s.
    val timeZones = ZoneId
        .getAvailableZoneIds()
        .filter { id ->
            val notExcluded = excludedIdPrefixes.none { id.startsWith(it) }
            val likelyNotObscure = !id.all { it.isUpperCase() || it.isDigit() || it == '-' }
            notExcluded && likelyNotObscure
        }
        .map { ZoneId.of(it) }
        .sortedBy { it.id }

    fun toTimeZone(str: String) = timeZones[str.toInt()]!!
}
