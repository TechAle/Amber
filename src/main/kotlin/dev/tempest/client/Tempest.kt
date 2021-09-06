package dev.tempest.client

import dev.tempest.api.event.EventManager
import dev.tempest.client.command.CommandManager.registerCommands
import dev.tempest.client.module.ModuleManager.registerModules
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Suppress("UNUSED_PARAMETER")
@Mod(modid = Tempest.MODID, name = Tempest.NAME, version = Tempest.VERSION)
class Tempest {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {}

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(EventManager)
        registerModules()
        registerCommands()
    }

    companion object {
        const val MODID = "tempest"
        const val NAME = "Tempest"
        const val VERSION = "0.1.0"
    }
}