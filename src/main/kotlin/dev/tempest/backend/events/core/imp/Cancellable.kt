package dev.tempest.backend.events.core.imp

/**
 * @author DarkMagician6
 * @since 08-27-2013
 * Translated to kotlin by TechAle
 */
interface Cancellable {
    fun isCancelled(): Boolean

    fun setCancelled(cancelled: Boolean)
}