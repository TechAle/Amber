package dev.amber.api.render

import dev.amber.api.variables.ABColor
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.math.Vec2f
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL32

object VertexUtil {

    fun prepareGl() {
        GlStateManager.pushMatrix()
        GL11.glLineWidth(1f)
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL32.GL_DEPTH_CLAMP)
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
        GlStateManager.disableAlpha()
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.depthMask(false)
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
    }

    fun releaseGL() {
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.disableBlend()
        GlStateManager.enableCull()
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.enableAlpha()
        GlStateManager.depthMask(true)
        GL11.glDisable(GL32.GL_DEPTH_CLAMP)
        GL11.glDisable(GL11.GL_LINE_SMOOTH)
        GlStateManager.color(1f, 1f, 1f)
        GL11.glLineWidth(1f)
        GlStateManager.popMatrix()
    }

    fun add(Coords: Vec2f) {
        GL11.glVertex2f(Coords.x, Coords.y)
    }

    fun add(Coords: Vec2f, color: ABColor) {
        color.glColor()
        GL11.glVertex2f(Coords.x, Coords.y)
    }



}