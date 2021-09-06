package dev.tempest.api.event

import dev.tempest.api.util.Globals.mc
import dev.tempest.client.command.CommandManager.onMessage
import dev.tempest.client.module.Module
import dev.tempest.client.module.ModuleManager.modules
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
        if (mc.player == null) return
        modules.filter(Module::enabled).forEach { it.onMessage(event) }
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