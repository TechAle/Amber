package dev.amber.backend.events.list

/*
    @author TechAle
    @since 07/09/21
 */

import dev.amber.backend.events.core.imp.Event
import net.minecraftforge.client.event.GuiOpenEvent

class EventGuiChange(
        val data : GuiOpenEvent
) : Event {
    companion object
}