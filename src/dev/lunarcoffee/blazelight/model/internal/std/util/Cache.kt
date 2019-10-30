package dev.lunarcoffee.blazelight.model.internal.std.util

import java.util.*

// Set-like cache backed by a weak referenced map.
class Cache<T : Comparable<T>>(private val limit: Int = 1024) : MutableSet<T> {
    private val storage = WeakHashMap<Int, T>()
    private var index = 0

    override val size = storage.size

    override fun contains(element: T) = element in storage.values
    override fun containsAll(elements: Collection<T>) = elements.all { it in storage.values }
    override fun isEmpty() = storage.values.isEmpty()
    override fun iterator() = storage.values.iterator()

    override fun add(element: T): Boolean {
        if (storage.size >= limit)
            remove(storage.entries.first().value)
        if (element in storage.values)
            remove(storage.entries.find { it.value == element }!!.value)
        return storage.put(index++, element) != null
    }

    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements)
            if (!add(element))
                return false
        return true
    }

    override fun clear() = storage.clear()
    override fun remove(element: T): Boolean {
        val key = storage.entries.find { it.value == element }?.key ?: return false
        storage.remove(key) ?: return false
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        for (element in elements)
            if (!remove(element))
                return false
        return true
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        for (value in storage.values)
            if (value !in elements && !remove(value))
                return false
        return true
    }
}
