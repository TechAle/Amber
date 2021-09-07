package dev.tempest.backend.event.core.imp;

/**
 * @author DarkMagician6
 * @since 08-27-2013
 */

public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
