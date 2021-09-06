package dev.amber.client.module

import dev.amber.client.module.modules.client.GUIModule
import dev.amber.client.module.modules.client.HUDModule
import dev.amber.client.module.modules.hud.ExampleHUD
import dev.amber.client.module.modules.misc.ExampleModule

/**
 * @author A2H
 */
object ModuleManager {
    val modules = arrayListOf<Module>()

    fun registerModules() {
        addModule(ExampleModule)
        addModule(GUIModule)
        addModule(HUDModule)
        addModule(ExampleHUD)
    }

    private fun addModule(m : Module) {
        modules.add(m)
    }
}