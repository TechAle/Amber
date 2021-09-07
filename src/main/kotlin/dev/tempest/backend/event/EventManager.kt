package dev.amber.api.event

import dev.amber.api.util.Globals.mc
import dev.amber.client.module.Module
import dev.amber.client.module.ModuleManager.modules
import dev.tempest.backend.managers.CommandManager.onMessage
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
object EventManager {

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (mc.player == null) return
        modules.filter(Module::enabled).forEach(Module::onTick)
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        onMessage(event)
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