package dev.tempest.backend.managers.list

import dev.amber.api.util.Globals.mc
import dev.amber.client.module.Module
import dev.tempest.backend.managers.list.ModuleManager.modules
import dev.tempest.backend.events.core.EventHandler
import dev.tempest.backend.events.core.EventTarget
import dev.tempest.backend.events.core.imp.Priority
import dev.tempest.backend.events.list.EventClientTick
import dev.tempest.backend.events.list.EventMessage
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard

/**
 * @author A2H
 */
@Suppress("UNUSED_PARAMETER")
object EventManager : manager {

    override fun onLoad() {
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (mc.player == null) return
        modules.filter(Module::enabled).forEach(Module::onTick)
    }

    /*
    EventHandler.call(EventClientTick(TickEvent.Phase.START))
    @EventTarget(Priority.HIGHEST)
    fun prova(event : EventClientTick) {

    }*/


    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        EventHandler.call(EventMessage(event))

    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Chat) {
        if (mc.renderManager.renderViewEntity == null) return
        modules.filter(Module::enabled).forEach(Module::onRender)
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onKeyPress(event: KeyInputEvent?) {
        if (Keyboard.getEventKeyState()) {
            val key = Keyboard.getEventKey()

            modules.forEach {
                if (key == it.key) {
                    it.toggle()
                }
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (mc.renderManager.renderViewEntity == null) return
        modules.filter(Module::enabled).forEach {it.onWorldRender(event)}
    }
}