@file:Suppress("DEPRECATION", "unused")

package dev.amber.frontend.module.modules.client


import dev.amber.api.render.RenderUtil2d
import dev.amber.api.render.VertexUtil
import dev.amber.api.variables.ABColor
import dev.amber.frontend.module.Module
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f

/**
 * @author TechAle
 * @since 12/09/21
 */
object testRendering : Module(category = Category.Client, "testRendering", true) {

    override fun onRender() {

        /// Text (text cannot be inside prepareGL and releaseGL)
        // Normal text
        RenderUtil2d.drawText("Quad", 0f, 0f, ABColor(255, 255, 255))
        // Gradient text
        RenderUtil2d.drawText("Circle", 80f, 0f, arrayOf(ABColor(255, 255, 255), ABColor(0, 0, 0)))
        // Multi gradient
        RenderUtil2d.drawText("Rounded Rect", 180f, 0f, arrayOf(ABColor(255, 0, 0), ABColor(0, 255, 0), ABColor(0, 0, 255)))
        /// Pictures
        // Normal
        RenderUtil2d.showPicture(400, 50, ResourceLocation("amber/img/logogradient.png"), 100, 100)
        // Color
        RenderUtil2d.showPicture(400, 200, ResourceLocation("amber/img/logowhite.png"), 50, 50, ABColor(0, 255, 255))

        VertexUtil.prepareGl()
        /// Rect
        // Normal rect
        RenderUtil2d.drawRect(Vec2f(10f, 30f), 50f, 50f, ABColor(255, 0, 0), )
        // Outline gradient rect 2 colors top bottom
        RenderUtil2d.drawRectOutline(Vec2f(10f, 90f), 50f, 50f, 5f, once = false,
                arrayOf(ABColor(0, 0, 255), ABColor(0, 255, 0)), topBottom = true)
        // Border rect with inside 1 color and outside 4 colors
        RenderUtil2d.drawRectBorder(Vec2f(10f, 160f), 50f, 50f, 1f,
                                    arrayOf(ABColor(0, 0, 0, 150)), true,
                arrayOf(ABColor(0, 0, 255), ABColor(0, 255, 0), ABColor(255, 0, 0), ABColor(255, 255, 255)), true)
        // Simple line 1 color
        RenderUtil2d.drawLine(Vec2f(70f, 0f), Vec2f(70f, 300f), 5f, ABColor(0,0,0))
        /// Circle
        // Filled
        RenderUtil2d.drawCircleFilled(Vec2f(100f, 60f), 20f, 720, ABColor(0, 255, 255))
        // 3/4 outline
        RenderUtil2d.drawCircleOutline(Vec2f(100f, 110f), 20f, 720, 5f,
                arrayOf(ABColor(0, 255, 0), ABColor(0, 0, 255)), Pair(0f, 270f), false )
        // Border
        RenderUtil2d.drawCircleBorder(Vec2f(100f, 170f), 20f, 720, 5f,
                arrayOf(ABColor(255, 255, 255), ABColor(0, 255, 255), ABColor(255, 255, 255)),
                arrayOf(ABColor(0, 0, 0), ABColor(255, 0, 0), ABColor(0, 0, 0)),
                Pair(0f, 366f), false)
        // 2 color line
        RenderUtil2d.drawLine(Vec2f(150f, 0f), Vec2f(150f, 300f), 5f, arrayOf(ABColor(0,255,255), ABColor(255, 255, 0)))
        RenderUtil2d.drawLine(Vec2f(300f, 0f), Vec2f(300f, 300f), 5f,
                arrayOf(ABColor(0, 0, 0), ABColor(255, 255, 255), ABColor(255, 255, 0), ABColor(0, 0, 255)))
        /// Rounded rect
        // Normal
        RenderUtil2d.drawRoundedRect(Vec2f(160f, 50f), 50f, 10f, 2f,
                arrayOf(ABColor(0, 0, 0), ABColor(255, 255, 255)))
        // Outline
        RenderUtil2d.drawRoundedRectOutline(Vec2f(160f, 100f), 50f, 50f, 5f, 3f, ABColor(0, 255, 0))
        // Border
        RenderUtil2d.drawRoundedRectBorder(Vec2f(160f, 170f), 50f, 50f, 10f, 3f,
                arrayOf(ABColor(255, 0, 0)), true,
                arrayOf(ABColor(255, 255, 0), ABColor(0, 255, 255)), false)

        VertexUtil.releaseGL()
    }

}