package dev.tempest.backend.events.list

/*
    @author TechAle
    @since 07/09/21
 */

import dev.tempest.backend.events.core.imp.EventCancellable
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventClientTick(
        val phase : TickEvent.Phase
) : EventCancellable() {
    companion object
}