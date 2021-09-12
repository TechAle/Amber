package dev.amber.frontend.module.modules.hud

import dev.amber.api.util.Globals.mc
import dev.amber.api.util.Globals.nullCheck
import dev.amber.frontend.module.Module
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