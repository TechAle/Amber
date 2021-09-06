package dev.tempest.client.module

import dev.tempest.client.module.modules.client.GUIModule
import dev.tempest.client.module.modules.client.HUDModule
import dev.tempest.client.module.modules.hud.ExampleHUD
import dev.tempest.client.module.modules.misc.ExampleModule

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