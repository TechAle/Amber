package dev.amber.client.module.modules.client

import dev.amber.api.util.Globals.mc
import dev.amber.api.util.Globals.nullCheck
import dev.amber.client.gui.GuiScreen
import dev.amber.client.module.Module
import org.lwjgl.input.Keyboard

/**
 * @Author A2H
 */
object GUIModule : Module(Category.Client, "ClickGui") {
    override var key = Keyboard.KEY_RSHIFT

    override fun onEnable() {
        nullCheck()
        mc.displayGuiScreen(GuiScreen())
    }
}