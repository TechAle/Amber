package dev.tempest.client.module.modules.client

import dev.tempest.api.util.Globals.mc
import dev.tempest.api.util.Globals.nullCheck
import dev.tempest.client.gui.HudScreen
import dev.tempest.client.module.Module
import org.lwjgl.input.Keyboard

object HUDModule : Module(Category.Client, "HUDEditor") {
    override var key = Keyboard.KEY_RCONTROL

    override fun onEnable() {
        nullCheck()
        GUIModule.enabled = false
        mc.displayGuiScreen(HudScreen())
    }
}