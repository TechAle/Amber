package dev.tempest.client.module

import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.input.Keyboard

/**
 * @author A2H
 */
open class Module(val category: Category, val name : String) {
    var enabled = false
    open var key = Keyboard.KEY_NONE

    enum class Category {
        Combat, Exploits, Movement, Misc, Render, Client, HUD
    }

    fun toggle() {
        enabled = !enabled
        if (enabled) onEnable()
        else onDisable()
    }

    open fun onEnable() {}

    open fun onDisable() {}

    open fun onTick() {}

    open fun onRender() {}

    open fun onMessage(event: ClientChatEvent) {}

    open fun onWorldRender(event: RenderWorldLastEvent) {}
}
