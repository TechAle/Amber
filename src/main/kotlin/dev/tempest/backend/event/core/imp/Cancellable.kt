package dev.tempest.backend.event.core.imp

/**
 * @author DarkMagician6
 * @since 08-27-2013
 */
interface Cancellable {
    fun isCancelled(): Boolean

    fun setCancelled(cancelled: Boolean)
}