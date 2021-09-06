package dev.tempest.client.module.modules.hud

import dev.tempest.api.util.Globals.mc
import dev.tempest.api.util.Globals.nullCheck
import dev.tempest.client.module.Module
import java.awt.Color

/**
 * @author Hoosiers
 */
object ExampleHUD : Module(category = Category.HUD, "ExampleHUD") {

    override fun onRender() {
        nullCheck()
        mc.fontRenderer.drawString("Hello Hi!", 10, 10, Color(255, 255, 255, 255).rgb)
    }
}