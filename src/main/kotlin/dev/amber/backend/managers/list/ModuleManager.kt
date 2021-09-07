package dev.amber.backend.managers.list

import dev.amber.client.module.Module
import dev.amber.client.module.modules.client.GUIModule
import dev.amber.client.module.modules.client.HUDModule
import dev.amber.client.module.modules.hud.ExampleHUD
import dev.amber.client.module.modules.misc.ExampleModule
import dev.amber.api.util.timer

/**
 * @author A2H
 */
object ModuleManager : manager {
    val modules = arrayListOf<Module>()

    override fun onLoad() {
        val count = timer();
        addModule(ExampleModule)
        addModule(GUIModule)
        addModule(HUDModule)
        addModule(ExampleHUD)
        count.endTimer("Manager Module")
    }

    private fun addModule(m : Module) {
        modules.add(m)
    }
}