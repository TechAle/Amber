package dev.amber.backend.events.list

/*
    @author TechAle
    @since 07/09/21
 */

import dev.amber.backend.events.core.imp.Event
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventRenderTick(
        val data : TickEvent.RenderTickEvent
) : Event {
    companion object
}