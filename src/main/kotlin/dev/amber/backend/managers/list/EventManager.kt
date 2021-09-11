package dev.amber.backend.managers.list

import dev.amber.api.util.Globals.mc
import dev.amber.client.module.Module
import dev.amber.backend.managers.list.ModuleManager.modules
import dev.amber.backend.events.core.EventHandler
import dev.amber.backend.events.list.EventMessage
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import net.minecraft.client.shader.ShaderGroup

import net.minecraftforge.fml.relauncher.ReflectionHelper
import net.minecraft.client.renderer.RenderGlobal
import java.lang.reflect.Field
import net.minecraft.util.ResourceLocation

import net.minecraft.client.Minecraft

import net.minecraft.client.renderer.EntityRenderer

import net.minecraftforge.client.event.GuiOpenEvent
import org.apache.commons.lang3.ArrayUtils


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
        val f: Field = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
    }

    private var _listShaders: Field? = null
    private var blurExclusions = ArrayList<String>()
    private var start: Long = 0

    @SubscribeEvent
    fun onGuiChange(event: GuiOpenEvent) {
        if (_listShaders == null) {
            // This was inverted lol
            _listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
        }
        if (Minecraft.getMinecraft().world != null) {
            val er = Minecraft.getMinecraft().entityRenderer
            val excluded = event.gui == null || blurExclusions.contains(event.gui.javaClass.name)
            if (!er.isShaderActive && !excluded) {
                er.loadShader(ResourceLocation("shaders/post/fade_in_blur.json"))
                /*
                    [14:47:33] [Client thread/WARN] [minecraft/EntityRenderer]: Failed to load shader: amber:image.png
                    net.minecraft.client.util.JsonException: Invalid image.png: File not found
                 */
                start = System.currentTimeMillis()
            } else if (er.isShaderActive && excluded) {
                er.stopUseShader()
                // new ResourceLocation("gamesense:capeblack.png")
            }
        }
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