package dev.amber.client.gui

import dev.amber.client.module.Module
import dev.tempest.backend.managers.list.ModuleManager.modules
import dev.amber.client.module.modules.client.HUDModule
import net.minecraft.client.gui.GuiScreen
import java.awt.Color

/**
 * @author A2H
 * @author Hoosiers
 */
class HudScreen : GuiScreen() {

    private val hudModuleList = arrayListOf<HudModules>()
    private var lmClicked = false
    private var rmClicked = false
    private var y = 3
    private val w = 105
    private var h = 15

    init {
        var yOffset = 3
        modules.forEach { module ->
            if (module.category != Module.Category.HUD) return@forEach
            yOffset += 14
            hudModuleList.add(HudModules(module, yOffset))
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun onGuiClosed() {
        HUDModule.enabled = false
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        fun isHover(X: Int, Y: Int, W: Int, H: Int, mX: Int, mY: Int): Boolean {
            return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H
        }

        drawRect(2, y - 1, 4 + w, y + h + 1, Color(0, 255, 0).rgb)
        drawRect(3, y, 3 + w, y + h, Color(64, 64, 64).rgb)
        mc.fontRenderer.drawString("HUDEditor", 6, y + 3, Color(196, 196, 196).rgb)

        hudModuleList.forEach { hL ->
            drawRect(2, hL.y, 4 + w, hL.y + h + 1,  Color(0, 255, 0).rgb)
            drawRect(3, hL.y, 3 + w, hL.y + h, Color(64, 64, 64).rgb)
            if (hL.module.enabled) {
                mc.fontRenderer.drawString(hL.module.name, 6, hL.y + 3,  Color(0, 255, 0).rgb)
            } else {
                mc.fontRenderer.drawString(hL.module.name, 6, hL.y + 3, Color(196, 196, 196).rgb)
            }
            if (lmClicked && isHover(3, hL.y, w, h,  mouseX, mouseY)) {
                hL.module.toggle()
                lmClicked = false
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

data class HudModules(var module: Module, val y: Int)