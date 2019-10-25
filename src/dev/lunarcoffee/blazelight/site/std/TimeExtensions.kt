package dev.lunarcoffee.blazelight.site.std

import dev.lunarcoffee.blazelight.model.internal.users.User
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoZonedDateTime
import java.time.format.DateTimeFormatter

private val DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("EEE dd/MM/yyyy 'at' h:mm:ss a")

fun <T : ChronoLocalDate> ChronoZonedDateTime<T>.format() = format(DEFAULT_FORMATTER)!!

fun Long.toUserTime(user: User) = ZonedDateTime.now(user.settings.zoneId)!!
fun Long.toUserTimeDisplay(user: User) = toUserTime(user).format()

fun Long.toTime(user: User?): ZonedDateTime {
    return if (user == null) ZonedDateTime.now(ZoneId.systemDefault())!! else toUserTime(user)
}

fun Long.toTimeDisplay(user: User?) = toTime(user).format()
