package dev.amber.api.render.gui

import dev.amber.api.render.RenderUtil2d
import dev.amber.api.render.VertexUtil
import dev.amber.api.util.Globals.mc
import dev.amber.api.util.MathUtils
import dev.amber.api.variables.ABColor
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f
import kotlin.math.sin

object background {

    /// Header
    // Logo
    fun drawLogo() {
        // Rect under the picture
        RenderUtil2d.drawRect(
                Start = Vec2f.ZERO,
                width = 154f,
                height = 60f,
                c = ABColor(0,0,0, 180)
        )
        // Border rectangle
        RenderUtil2d.drawRect(
                Start = Vec2f(0f, 58f),
                width = 154f,
                height = 2f,
                c = ABColor(255, 0, 0)
        )

        // Triangle
        RenderUtil2d.drawTriangle(
                pos1 = Vec2f(154f, 0f),
                pos2 = Vec2f(154f, 58f),
                pos3 = Vec2f(175f, 0f),
                ABColor(0, 0, 0, 180)
        )

        // Above triangle
        RenderUtil2d.drawLine(
                start = Vec2f(154f, 59f),
                end = Vec2f(175f, 0f),
                c = ABColor(255, 0, 0),
                lineWidth = 3f
        )

        // Release for showing the picture+text
        VertexUtil.releaseGL()
        // Amber picture
        RenderUtil2d.showPicture(x = 5, y = 5,
                resourceLocation = ResourceLocation("amber/img/logogradient.png"),
                width = 47, height = 45)

        // Tecture
        RenderUtil2d.drawText(text = "Amber", x = 60f, y= 16f,
                color = ABColor(255, 255, 255),
                fontSize = 3f)

        // Prepare gl again
        VertexUtil.prepareGl()
    }

    /// Bottom

    // Particles
    fun drawParticles() {

    }

    // Header
    private val number = 5
    private val differenceSpeed = .3
    private var timer = 0.0
    private val speed = .005
    private val staticColorHeight = 10f
    private val addHeight = 60f
    private val varHeight = 20f
    private val startAlpha = 255
    private val finalAlpha = 0
    private val finalAlphaAnimation = true
    fun drawDynamicBoxes() {
        // Get size of window
        val width = ScaledResolution(mc).scaledWidth.toFloat()
        val height = ScaledResolution(mc).scaledHeight.toFloat()
        if (number == 0) {
            return
        } else
            if (number <= 2)
                drawDynamicBox(height, 0f, width, differenceSpeed)
            else {

                val space = width / number
                var x = 0f
                val middleBox: Boolean
                var numbers = number
                if (numbers % 2 == 1) {
                    numbers -= 1
                    middleBox = true
                } else middleBox = false

                for(i in 0..numbers/2) {
                    drawDynamicBox(height, x, space, differenceSpeed * i)
                    x += space
                }

                if (middleBox) {
                    drawDynamicBox(height, x, space, differenceSpeed * numbers / 2 + differenceSpeed)
                    x += space
                }

                for(i in numbers/2 downTo 0 step 1) {
                    drawDynamicBox(height, x, space, differenceSpeed * i)
                    x += space
                }

            }
    }

    private fun drawDynamicBox(heightWindow: Float, x: Float, width: Float, differenceSpeed: Double) {

        val nowHeight = sin(timer + differenceSpeed) * varHeight

        val finalAlphaNow: Float
        val avgHeight = heightWindow - staticColorHeight
        if (finalAlphaAnimation) {
            val startHeight = avgHeight - varHeight
            val endHeight = avgHeight + varHeight
            val rnHeight = avgHeight + nowHeight.toFloat()

            val percent = MathUtils.percentage(startHeight, endHeight, rnHeight)

            val alpha = (startAlpha - finalAlpha) * percent
            finalAlphaNow = startAlpha - alpha
        } else finalAlphaNow = finalAlpha.toFloat()

        val staticColor = ABColor(255, 50, 0, startAlpha)
        val startColor = ABColor(255, 50, 0, startAlpha)
        val endColor = ABColor(255, 50, 0, finalAlphaNow.toInt())

        RenderUtil2d.drawRect(Vec2f(x, heightWindow), width, -staticColorHeight, staticColor)
        RenderUtil2d.drawRect(Vec2f(x, avgHeight), width, - addHeight - nowHeight.toFloat(), false,
                arrayOf(startColor, endColor), true)

        timer += speed
    }


}