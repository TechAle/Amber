package dev.amber.client

import dev.amber.api.util.timer
import dev.amber.backend.managers.list.ModuleManager
import dev.amber.backend.events.core.EventHandler
import dev.amber.backend.managers.list.CommandManager
import dev.amber.backend.managers.list.EventManager
import dev.amber.backend.managers.list.manager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.util.logging.Logger

/*
    @author TechAle
    @since 07/09/21
 */

@Suppress("UNUSED_PARAMETER")
@Mod(modid = Amber.MODID, name = Amber.NAME, version = Amber.VERSION)
class Amber {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {}

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        val time = timer()
        MinecraftForge.EVENT_BUS.register(EventManager)
        loadManager(CommandManager)
        loadManager(ModuleManager)
        loadManager(EventManager)
        time.endTimer("Init Amber")
    }

    fun loadManager(manager : manager) {
        EventHandler.register(manager)
        manager.onLoad()
    }

    companion object {
        const val MODID = "amber"
        const val NAME = "Amber"
        const val VERSION = "0.1.0"
        val LOGGER = Logger.getLogger(NAME)
    }
}