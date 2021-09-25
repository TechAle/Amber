package dev.amber.client.gui

import dev.amber.api.render.RenderUtil2d
import dev.amber.api.render.VertexUtil
import dev.amber.api.util.MessageUtil
import dev.amber.api.variables.ABColor
import dev.amber.backend.managers.list.ModuleManager.modules
import dev.amber.frontend.module.Module
import dev.amber.frontend.module.modules.client.GUIModule
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.Vec2f
import org.lwjgl.opengl.GL11
import java.awt.Color

/**
 * @author A2H
 */
class GuiScreen : GuiScreen() {

    private val categoriesList = arrayListOf<Categories>()
    private val moduleList = arrayListOf<Modules>()
    private var lmClicked = false
    private var rmClicked = false
    private var y = 3
    private val w = 105
    private var h = 15

    init {
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
        }
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

    }

    // Just draw the animations on the bottom of the gui
    private fun drawBottom() {


        // Release openGl
        VertexUtil.releaseGL()
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