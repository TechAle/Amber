package dev.amber.backend.managers.list

import dev.amber.api.util.LOGGER
import dev.amber.frontend.module.Module
import dev.amber.frontend.module.modules.client.*
import dev.amber.frontend.module.modules.misc.*
import dev.amber.frontend.module.modules.hud.*

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
        addModule(BottomColor)
        LOGGER.endTimer("Module Manager")
    }

    private fun addModule(m : Module) {
        modules.add(m)
    }
}