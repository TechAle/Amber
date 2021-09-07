package dev.amber.backend.events.core.imp

/**
 * @author DarkMagician6
 * @since 08-27-2013
 * Translated to kotlin by TechAle
 */
abstract class EventCancellable : Event, Cancellable {
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }
}