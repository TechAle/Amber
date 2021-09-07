package dev.tempest.backend.event

import dev.tempest.backend.event.core.imp.EventCancellable
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventClientTick(
        val phase : TickEvent.Phase
) : EventCancellable() {
    companion object
}