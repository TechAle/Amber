package dev.amber.backend.managers.list

import dev.amber.api.util.Globals.mc
import dev.amber.backend.events.core.EventHandler
import dev.amber.backend.events.list.EventGuiChange
import dev.amber.backend.events.list.EventMessage
import dev.amber.backend.events.list.EventRenderTick
import dev.amber.backend.managers.list.ModuleManager.modules
import dev.amber.client.module.Module
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import org.lwjgl.input.Keyboard
import java.lang.reflect.Field


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

    private var _listShaders: Field? = null
    private var blurExclusions = ArrayList<String>()
    private var start: Long = 0
    private var fadeTime = 0

    @SubscribeEvent
    fun onGuiChange(event: GuiOpenEvent) {
        EventHandler.call(EventGuiChange(event))
        /*
        if (_listShaders == null) {
            // This was inverted lol
            _listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
        }
        if (Minecraft.getMinecraft().world != null) {
            val er = Minecraft.getMinecraft().entityRenderer
            val excluded = event.gui == null || blurExclusions.contains(event.gui.javaClass.name)
            if (!er.isShaderActive && !excluded) {
                er.loadShader(ResourceLocation("amber/fade_in_blur.json"))
                start = System.currentTimeMillis()
            } else if (er.isShaderActive && excluded) {
                er.stopUseShader()
            }
        }*/
    }

    private fun getProgress(): Float {
        return Math.min((System.currentTimeMillis() - start) / (fadeTime.toFloat()), 1f)
    }

    @SubscribeEvent
    fun onRenderTick(event: RenderTickEvent) {
        EventHandler.call(EventRenderTick(event))
        /*
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive) {
            val sg = Minecraft.getMinecraft().entityRenderer.shaderGroup
            try {
                val shaders: List<Shader?> = _listShaders?.get(sg) as List<Shader?>
                for (s in shaders) {
                    val su: ShaderUniform? = s?.getShaderManager()?.getShaderUniform("Progress")
                    if (su != null) {
                        su.set(getProgress())
                    }
                }
            } catch (e: IllegalArgumentException) {
                Throwables.propagate(e)
            } catch (e: IllegalAccessException) {
                Throwables.propagate(e)
            }
        }*/
    }


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