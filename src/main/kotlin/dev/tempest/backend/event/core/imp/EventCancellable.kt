package dev.tempest.backend.event.core.imp

/**
 * @author DarkMagician6
 * @since 08-27-2013
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