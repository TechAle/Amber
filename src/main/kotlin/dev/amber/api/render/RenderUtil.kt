package dev.amber.api.render

import com.sun.javafx.geom.Vec2d
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL32
import org.lwjgl.opengl.GL11.glPopAttrib

import org.lwjgl.opengl.GL11.glScaled

import org.lwjgl.opengl.GL11.GL_TEXTURE_2D

import org.lwjgl.opengl.GL11.glEnable

import org.lwjgl.opengl.GL11.GL_BLEND

import org.lwjgl.opengl.GL11.glDisable

import org.lwjgl.opengl.GL11.GL_LINE_SMOOTH

import org.lwjgl.opengl.GL11.glVertex2d

import org.lwjgl.opengl.GL11.GL_POLYGON

import org.lwjgl.opengl.GL11.glColor4f

import org.lwjgl.opengl.GL11.GL_POINTS

import org.lwjgl.opengl.GL11.glPushAttrib
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.*


object RenderUtil {

    /**
     * Setup Gl states
     */
    fun prepareGL() {
        GlStateManager.pushMatrix()
        glLineWidth(1f)
        glEnable(GL_LINE_SMOOTH)
        glEnable(GL32.GL_DEPTH_CLAMP)
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST)
        GlStateManager.disableAlpha()
        GlStateManager.shadeModel(GL_SMOOTH)
        GlStateManager.disableCull()
        GlStateManager.enableBlend()
        GlStateManager.depthMask(false)
        GlStateManager.disableTexture2D()
        GlStateManager.disableLighting()
    }

    /**
     * Reverts Gl states
     */
    fun releaseGL() {
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.disableBlend()
        GlStateManager.enableCull()
        GlStateManager.shadeModel(GL_FLAT)
        GlStateManager.enableAlpha()
        GlStateManager.depthMask(true)
        glDisable(GL32.GL_DEPTH_CLAMP)
        glDisable(GL_LINE_SMOOTH)
        GlStateManager.color(1f, 1f, 1f)
        glLineWidth(1f)
        GlStateManager.popMatrix()
    }

    fun drawRect(x: Float, y: Float, width: Float, height: Float, c: ABColor, once : Boolean = false) {
        if (once)
            prepareGL()
        glBegin(GL_QUADS)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)
        glVertex2f(x, y)
        glVertex2f(x, y + height)
        glVertex2f(x + width, y + height)
        glVertex2f(x + width, y)
        glEnd()
        if (once)
            releaseGL()
    }

    fun drawRectBorder(x: Float, y: Float, width: Float, height: Float, borderWidth: Float, insideC: ABColor, borderColor: ABColor, once : Boolean = true ) {
        if (once)
            prepareGL()

        // Draw inside
        drawRect(x + borderWidth, y + borderWidth, width - borderWidth , height - borderWidth, insideC)
        /// Draw border
        drawBorder(x, y, width, height, borderWidth, borderColor, once)

        if (once)
            releaseGL()
    }

    fun drawBorder(x: Float, y: Float, width: Float, height: Float, borderWidth: Float, borderColor: ABColor, once: Boolean = true) {
        if (once)
            prepareGL()

        // Top
        drawRect(x, y, width , borderWidth, borderColor)
        // Bottom
        drawRect(x, y + height, width, borderWidth, borderColor)
        // Left
        drawRect(x, y + borderWidth, borderWidth, height - borderWidth, borderColor)
        // Right
        drawRect(x + width, y + borderWidth, -borderWidth, height - borderWidth, borderColor)

        if (once)
            releaseGL()
    }

    fun drawRoundedRect(x: Double, y: Double, width: Float, height: Float, radius: Float, c: ABColor) {
        prepareGL()

        /*
        // Draw up
        drawRect(x + radius, y, width - radius * 2, radius, color)
        // Draw down
        drawRect(x + radius, y + height, width - radius * 2,  -radius, color)
        // Draw left
        drawRect(x, y + radius, radius, height - radius * 2, color)
        // Draw Right
        drawRect(width + x, y + radius, -radius, height - radius * 2, color)
        // Draw body
        drawRect(x + radius, y + radius, width - radius * 2, height - radius * 2, color)
        */

        glBegin(GL_POLYGON)
        glColor4f(c.getRed().toFloat() / 255, c.getGreen().toFloat() / 255, c.getBlue().toFloat() / 255, c.getAlpha().toFloat() / 255)

        glVertex2d(x, y)
        glVertex2d(x + 10, y + 1)
        glVertex2d(x + 20, y + 3)
        glVertex2d(x + 30, y + 5)
        glVertex2d(x + 40, y + 10)

        glEnd()



        releaseGL()
    }



}

