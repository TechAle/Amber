@file:Suppress("DEPRECATION", "unused")

package dev.amber.frontend.module.modules.client

import com.google.common.base.Throwables
import dev.amber.backend.events.core.EventTarget
import dev.amber.backend.events.core.imp.Priority
import dev.amber.backend.events.list.EventGuiChange
import dev.amber.backend.events.list.EventRenderTick
import dev.amber.client.module.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.shader.Shader
import net.minecraft.client.shader.ShaderGroup
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper
import java.lang.reflect.Field

/**
 * @author TechAle
 * @since 12/09/21
 * Thanks to tterrag1098 for the blur mod (https://github.com/tterrag1098/Blur)
 */
object Blur : Module(category = Category.Client, "Blur", true) {

    private var listShaders: Field? = null
    private var blurExclusions = ArrayList<String>()
    private var start: Long = 0
    private var fadeTime = 0

    @EventTarget(Priority.HIGHEST)
    fun guiChangeEvent(event : EventGuiChange) {
        if (listShaders == null) {
            // This was inverted lol
            listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
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
        if (listShaders == null) {
            // This was inverted lol
            listShaders = ReflectionHelper.findField(ShaderGroup::class.java, "listShaders", "field_148031_d")
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
    fun renderTickEvent(event : EventRenderTick) {
        if (event.data.phase == TickEvent.Phase.END && Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().entityRenderer.isShaderActive) {
            val sg = Minecraft.getMinecraft().entityRenderer.shaderGroup
            try {
                @Suppress("UNCHECKED_CAST") val shaders: List<Shader?> = listShaders?.get(sg) as List<Shader?>
                for (s in shaders) {
                    s?.shaderManager?.getShaderUniform("Progress")?.set(getProgress())
                }
            } catch (e: IllegalArgumentException) {
                Throwables.propagate(e)
            } catch (e: IllegalAccessException) {
                Throwables.propagate(e)
            }
        }
    }



    private fun getProgress(): Float {
        return ((System.currentTimeMillis() - start) / (fadeTime.toFloat())).coerceAtMost(1f)
    }
}