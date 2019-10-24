package dev.lunarcoffee.blazelight.model.internal.std

interface DBCacheable<T> {
    suspend fun cacheFromDB(id: Long): T?
}
