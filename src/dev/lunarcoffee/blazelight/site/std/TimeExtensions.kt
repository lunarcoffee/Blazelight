package dev.lunarcoffee.blazelight.site.std

import dev.lunarcoffee.blazelight.model.internal.users.User
import java.time.*
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoZonedDateTime
import java.time.format.DateTimeFormatter

private val DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy 'at' h:mm:ss a")

fun <T : ChronoLocalDate> ChronoZonedDateTime<T>.format() = format(DEFAULT_FORMATTER)!!

fun Long.toTimeDisplay(user: User?) = toTime(user).format()
fun Long.toTime(user: User?): ZonedDateTime {
    return ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        user?.settings?.zoneId ?: ZoneId.systemDefault()
    )!!
}
