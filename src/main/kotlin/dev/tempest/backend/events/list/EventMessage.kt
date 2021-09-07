package dev.tempest.backend.events.list

/*
    @author TechAle
    @since 07/09/21
 */

import dev.tempest.backend.events.core.imp.Event
import dev.tempest.backend.events.core.imp.EventCancellable
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventMessage(
        text : ClientChatEvent
) : EventCancellable() {
    val message = text

}