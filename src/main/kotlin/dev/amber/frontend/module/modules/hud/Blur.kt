package dev.amber.client.module.modules.hud

import com.google.common.base.Throwables
import dev.amber.api.util.Globals.mc
import dev.amber.api.util.Globals.nullCheck
import dev.amber.backend.events.core.EventTarget
import dev.amber.backend.events.core.imp.Priority
import dev.amber.backend.events.list.EventGuiChange
import dev.amber.backend.events.list.EventMessage
import dev.amber.backend.events.list.EventRenderTick
import dev.amber.backend.managers.list.EventManager
import dev.amber.client.module.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.shader.Shader
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.client.shader.ShaderUniform
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.awt.Color
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

/**
 * @author Hoosiers
 */
object Blur : Module(category = Category.Client, "Blur") {

    private var _listShaders: Field? = null
    private var blurExclusions = ArrayList<String>()
    private var start: Long = 0
    private var fadeTime = 0

    @EventTarget(Priority.HIGHEST)
    fun GuiChangeEvent(event : EventGuiChange) {
        if (_listShaders == null) {
            // This was inverted lol
            _listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
        }
        if (Minecraft.getMinecraft().world != null) {
            val er = Minecraft.getMinecraft().entityRenderer
            val excluded = event.data.gui == null || blurExclusions.contains(event.data.gui.javaClass.name)
            if (!er.isShaderActive && !excluded) {
                er.loadShader(ResourceLocation("amber/fade_in_blur.json"))
                start = System.currentTimeMillis()
            } else if (er.isShaderActive && excluded) {
                er.stopUseShader()
            }
        }
    }

    override fun onEnable() {
        if (_listShaders == null) {
            // This was inverted lol
            _listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
        }
        if (Minecraft.getMinecraft().world != null) {
            val er = Minecraft.getMinecraft().entityRenderer
            if (!er.isShaderActive) {
                er.loadShader(ResourceLocation("amber/fade_in_blur.json"))
                start = System.currentTimeMillis()
            }
        }

    }

    override fun onDisable() {
        if (Minecraft.getMinecraft().world != null) {
            val er = Minecraft.getMinecraft().entityRenderer
            if (er.isShaderActive) {
                er.stopUseShader()
            }
        }
    }

    @EventTarget(Priority.HIGHEST)
    fun RenderTickEvent(event : EventRenderTick) {
        if (event.data.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive) {
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
        }
    }



    private fun getProgress(): Float {
        return Math.min((System.currentTimeMillis() - start) / (fadeTime.toFloat()), 1f)
    }
}