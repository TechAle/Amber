package dev.amber.client.gui

import dev.amber.backend.managers.list.ModuleManager.modules
import dev.amber.client.module.Module
import dev.amber.client.module.modules.client.GUIModule
import net.minecraft.client.gui.GuiScreen
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

        lmClicked = false
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (mouseButton == 0) {
            lmClicked = true
        }
        if (mouseButton == 1) {
            rmClicked = true
        }
    }
}

data class Categories(var name: Module.Category, val x: Int)

data class Modules(var module: Module, val y: Int, var category: Module.Category)