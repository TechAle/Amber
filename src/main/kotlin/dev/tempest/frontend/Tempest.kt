package dev.amber.client

import dev.amber.api.event.EventManager
import dev.amber.client.module.ModuleManager.registerModules
import dev.tempest.backend.managers.CommandManager.registerCommands
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Suppress("UNUSED_PARAMETER")
@Mod(modid = Amber.MODID, name = Amber.NAME, version = Amber.VERSION)
class Amber {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {}

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(EventManager)
        registerModules()
        registerCommands()
    }

    companion object {
        const val MODID = "amber"
        const val NAME = "Amber"
        const val VERSION = "0.1.0"
    }
}