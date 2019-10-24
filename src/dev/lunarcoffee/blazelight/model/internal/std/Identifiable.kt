package dev.lunarcoffee.blazelight.model.internal.std

interface Identifiable : Comparable<Identifiable> {
    val id: Long

    // This should be used only for testing equality, since there is no concept of an identifiable
    // object being less than or greater than another.
    override fun compareTo(other: Identifiable): Int {
        return (id - other.id).toInt()
    }
}
