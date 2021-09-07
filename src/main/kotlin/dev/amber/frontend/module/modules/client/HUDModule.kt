package dev.amber.client.module.modules.client

import dev.amber.api.util.Globals.mc
import dev.amber.api.util.Globals.nullCheck
import dev.amber.client.gui.HudScreen
import dev.amber.client.module.Module
import org.lwjgl.input.Keyboard

object HUDModule : Module(Category.Client, "HUDEditor") {
    override var key = Keyboard.KEY_RCONTROL

    override fun onEnable() {
        nullCheck()
        GUIModule.enabled = false
        mc.displayGuiScreen(HudScreen())
    }
}