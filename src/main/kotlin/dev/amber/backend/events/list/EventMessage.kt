package dev.amber.backend.events.list

/*
    @author TechAle
    @since 07/09/21
 */

import dev.amber.backend.events.core.imp.Event
import dev.amber.backend.events.core.imp.EventCancellable
import net.minecraftforge.client.event.ClientChatEvent

class EventMessage(
        text : ClientChatEvent
) : Event {
    val message = text

}