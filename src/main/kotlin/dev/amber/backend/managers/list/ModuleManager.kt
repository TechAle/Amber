package dev.amber.backend.managers.list

import dev.amber.api.util.LOGGER
import dev.amber.client.module.Module
import dev.amber.client.module.modules.client.GUIModule
import dev.amber.client.module.modules.client.HUDModule
import dev.amber.client.module.modules.hud.Blur
import dev.amber.client.module.modules.hud.ExampleHUD
import dev.amber.client.module.modules.misc.ExampleModule

/**
 * @author A2H
 */
object ModuleManager : manager {
    val modules = arrayListOf<Module>()

    override fun onLoad() {
        LOGGER.startTimer("Module Manager")
        addModule(ExampleModule)
        addModule(GUIModule)
        addModule(HUDModule)
        addModule(ExampleHUD)
        addModule(Blur)
        LOGGER.endTimer("Module Manager")
    }

    private fun addModule(m : Module) {
        modules.add(m)
    }
}