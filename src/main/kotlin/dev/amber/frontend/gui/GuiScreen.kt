package dev.amber.client.gui

import dev.amber.api.render.RenderUtil2d
import dev.amber.api.render.VertexUtil
import dev.amber.api.util.MathUtils
import dev.amber.api.util.MessageUtil
import dev.amber.api.variables.ABColor
import dev.amber.frontend.module.Module
import dev.amber.frontend.module.modules.client.GUIModule
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f
import kotlin.math.sin

/**
 * @author A2H
 */
class GuiScreen : GuiScreen() {

    private val categoriesList = arrayListOf<Categories>()
    private val moduleList = arrayListOf<Modules>()

    init {
        /*
        var xOffset = 3
        Module.Category.values().forEach { category ->
            if (category == Module.Category.HUD) return@forEach
            categoriesList.add(Categories(category, xOffset))
            xOffset += 114
            var yOffset = 3
            modules.forEach { module ->
                if (category == module.category) {
                    yOffset += 14
                    moduleList.add(Modules(module, yOffset, module.category))
                }
            }
        }*/
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun onGuiClosed() {
        GUIModule.enabled = false
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        /*
        fun isHover(X: Int, Y: Int, W: Int, H: Int, mX: Int, mY: Int): Boolean {
            return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H
        }
        categoriesList.forEach { category ->
            drawRect(category.x - 1, y - 1, category.x + 1 + w, y + h + 1, Color(0, 255, 0).rgb)
            drawRect(category.x, y, category.x + w, y + h, Color(64, 64, 64).rgb)
            mc.fontRenderer.drawString(category.name.toString(), category.x + 3, y + 3, Color(196, 196, 196).rgb)

            moduleList.forEach { mL ->
                if (category.name == mL.category) {
                    drawRect(category.x - 1, mL.y, category.x + w + 1, mL.y + h + 1,  Color(0, 255, 0).rgb)
                    drawRect(category.x, mL.y, category.x + w, mL.y + h, Color(64, 64, 64).rgb)
                    if (mL.module.enabled) {
                        mc.fontRenderer.drawString(mL.module.name, category.x + 3, mL.y + 3,  Color(0, 255, 0).rgb)
                    } else {
                        mc.fontRenderer.drawString(mL.module.name, category.x + 3, mL.y + 3, Color(196, 196, 196).rgb)
                    }
                    if (lmClicked && isHover(category.x, mL.y, w, h,  mouseX, mouseY)) {
                        mL.module.toggle()
                        lmClicked = false
                    }
                }
            }
        }
        lmClicked = false*/


        drawHeader()



        drawBottom()

    }

    // Just draw the header of the gui
    private fun drawHeader() {

        // Prepare opengGL
        VertexUtil.prepareGl()
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

    // Just draw the animations on the bottom of the gui
    private fun drawBottom() {

        drawDynamicBoxes()

        // Release openGl
        VertexUtil.releaseGL()
    }

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
    private fun drawDynamicBoxes() {
        // Get size of window
        val width = ScaledResolution(mc).scaledWidth.toFloat()
        val height = ScaledResolution(mc).scaledHeight.toFloat()
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

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            MessageUtil.sendClientMessage("Clicked 0")
        }
        else if (mouseButton == 1) {
            MessageUtil.sendClientMessage("Clicked 1")
        }
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            MessageUtil.sendClientMessage("Released 0")
        }
        else if (mouseButton == 1) {
            MessageUtil.sendClientMessage("Released 1")
        }
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, mouseButton: Int, timeSinceLastClick: Long) {
        if (mouseButton == 0) {
            //MessageUtil.sendClientMessage("Moved 0")
        }
        else if (mouseButton == 1) {
            //MessageUtil.sendClientMessage("Moved 1")
        }
    }


}

data class Categories(var name: Module.Category, val x: Int)

data class Modules(var module: Module, val y: Int, var category: Module.Category)


/// In mememory of every sketches
// Watermark
/*
    First scatch watermark
    // Prepare opengGL
    VertexUtil.prepareGl()
    // Rect under the picture
    RenderUtil2d.drawRect(
            Start = Vec2f.ZERO,
            width = 61f,
            height = 50f,
            c = ABColor(0,0,0, 180)
    )
    GlStateManager.pushMatrix()
    GL11.glScalef(1f, .4f, 1f)
    // Circle belove
    RenderUtil2d.drawCircleBorder(
            center = Vec2f(0f, 125f),
            radius = 63f,
            segments = 1000,
            insideC = arrayOf(ABColor(0, 0, 0, 180)),
            outsideC = arrayOf(ABColor(0, 0, 0), ABColor(255, 0, 0)),
            angleRange = Pair(90f, 180f))
    GlStateManager.popMatrix()
    // Border rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 48f),
            width = 93f,
            height = 2f,
            c = ABColor(255, 0, 0)
    )

    // Inside rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 0f),
            width = 93f,
            height = 48f,
            c = ABColor(0, 0,0, 180)
    )

    // Triangle
    RenderUtil2d.drawTriangle(
            pos1 = Vec2f(154f, 0f),
            pos2 = Vec2f(154f, 48f),
            pos3 = Vec2f(175f, 0f),
            ABColor(0, 0, 0, 180)
    )

    // Above triangle
    RenderUtil2d.drawLine(
            start = Vec2f(154f, 49f),
            end = Vec2f(175f, 0f),
            c = ABColor(255, 0, 0),
            lineWidth = 3f
    )

    // Release for showing the picture+text
    VertexUtil.releaseGL()
    // Amber picture
    RenderUtil2d.showPicture(x = 5, y = 5,
                             resourceLocation = ResourceLocation("amber/img/logogradient.png"),
                             width = 45, height = 45)

    // Tecture
    RenderUtil2d.drawText(text = "Amber", x = 60f, y= 16f,
                          color = ABColor(255, 255, 255),
                          fontSize = 3f)

    // Prepare gl again
    VertexUtil.prepareGl()
 */
/*
    Second sketch watermark

    // Prepare opengGL
    VertexUtil.prepareGl()
    // Rect under the picture
    RenderUtil2d.drawRect(
            Start = Vec2f.ZERO,
            width = 61f,
            height = 50f,
            c = ABColor(0,0,0, 180)
    )
    GlStateManager.pushMatrix()
    GL11.glScalef(1f, .4f, 1f)
    // Circle belove
    RenderUtil2d.drawCircleBorder(
            center = Vec2f(0f, 125f),
            radius = 63f,
            segments = 1000,
            insideC = arrayOf(ABColor(0, 0, 0, 180)),
            outsideC = arrayOf(ABColor(0, 0, 0), ABColor(255, 0, 0)),
            angleRange = Pair(90f, 180f))
    GlStateManager.popMatrix()
    // Border rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 48f),
            width = 73f,
            height = 2f,
            c = ABColor(255, 0, 0)
    )

    // Inside rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 0f),
            width = 93f,
            height = 48f,
            c = ABColor(0, 0,0, 180)
    )

    // Triangle
    RenderUtil2d.drawTriangle(
            pos1 = Vec2f(154f, 0f),
            pos2 = Vec2f(154f, 48f),
            pos3 = Vec2f(175f, 0f),
            ABColor(0, 0, 0, 180)
    )

    // Release for showing the picture+text
    VertexUtil.releaseGL()
    // Amber picture
    RenderUtil2d.showPicture(x = 5, y = 5,
                             resourceLocation = ResourceLocation("amber/img/logogradient.png"),
                             width = 45, height = 45)

    // Tecture
    RenderUtil2d.drawText(text = "Amber", x = 60f, y= 16f,
                          color = ABColor(255, 255, 255),
                          fontSize = 3f)

    // Prepare gl again
    VertexUtil.prepareGl()
 */
/*
    Third sketch watermark
    // Prepare opengGL
    VertexUtil.prepareGl()
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
            width = 45, height = 45)

    // Tecture
    RenderUtil2d.drawText(text = "Amber", x = 60f, y= 16f,
            color = ABColor(255, 255, 255),
            fontSize = 3f)

    // Prepare gl again
    VertexUtil.prepareGl()
 */
/*
    Four sketch watermark
    // Prepare opengGL
    VertexUtil.prepareGl()
    // Rect under the picture
    RenderUtil2d.drawRect(
            Start = Vec2f.ZERO,
            width = 61f,
            height = 50f,
            c = ABColor(0,0,0, 180)
    )

    RenderUtil2d.drawTriangle(
            pos1 = Vec2f(0f, 50f),
            pos2 = Vec2f(61f, 50f),
            pos3 = Vec2f(0f, 90f),
            c = ABColor(0, 0, 0, 180)
    )

    RenderUtil2d.drawLine(
            start = Vec2f(0f, 90f),
            end = Vec2f(61f, 49f),
            c = ABColor(255, 0, 0),
            lineWidth = 3f
    )

    // Border rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 48f),
            width = 83f,
            height = 2f,
            c = ABColor(255, 0, 0)
    )

    // Inside rectangle
    RenderUtil2d.drawRect(
            Start = Vec2f(61f, 0f),
            width = 83f,
            height = 48f,
            c = ABColor(0, 0,0, 180)
    )

    // Triangle
    RenderUtil2d.drawTriangle(
            pos1 = Vec2f(144f, 0f),
            pos2 = Vec2f(144f, 48f),
            pos3 = Vec2f(175f, 0f),
            ABColor(0, 0, 0, 180)
    )

    // Above triangle
    RenderUtil2d.drawLine(
            start = Vec2f(144f, 49f),
            end = Vec2f(175f, 0f),
            c = ABColor(255, 0, 0),
            lineWidth = 3f
    )

    // Release for showing the picture+text
    VertexUtil.releaseGL()
    // Amber picture
    RenderUtil2d.showPicture(x = 5, y = 5,
            resourceLocation = ResourceLocation("amber/img/logogradient.png"),
            width = 45, height = 45)

    // Tecture
    RenderUtil2d.drawText(text = "Amber", x = 60f, y= 16f,
            color = ABColor(255, 255, 255),
            fontSize = 3f)

    // Prepare gl again
    VertexUtil.prepareGl()
 */