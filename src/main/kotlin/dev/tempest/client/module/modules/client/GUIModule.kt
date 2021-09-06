package dev.tempest.client.module.modules.client

import dev.tempest.api.util.Globals.mc
import dev.tempest.api.util.Globals.nullCheck
import dev.tempest.client.gui.GuiScreen
import dev.tempest.client.module.Module
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