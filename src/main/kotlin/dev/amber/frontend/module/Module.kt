package dev.amber.frontend.module

import dev.amber.backend.events.core.EventHandler
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.input.Keyboard

/**
 * @author A2H
 */
open class Module(val category: Category, val name : String, enable : Boolean = false) {
    var enabled = enable
    open var key = Keyboard.KEY_NONE

    init {
        if (enable) {
            enable()
        }
    }

    enum class Category {
        Combat, Exploits, Movement, Misc, Render, Client, HUD
    }

    fun toggle() {
        enabled = !enabled
        if (enabled) enable()
        else disable()
    }

    fun enable() {
        onEnable()
        EventHandler.register(this)
    }

    fun disable() {
        onDisable()
        EventHandler.unregister(this)
    }


    open fun onEnable() {
    }

    open fun onDisable() {
    }

    open fun onTick() {}

    open fun onRender() {}

    open fun onWorldRender(event: RenderWorldLastEvent) {}
}
