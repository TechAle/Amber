package dev.tempest.api.util

import net.minecraft.client.Minecraft

/**
 * @author A2H
 * @author Hoosiers
 */
object Globals {
    val mc: Minecraft = Minecraft.getMinecraft()

    fun nullCheck() {
        if (mc.player == null || mc.world == null) return
    }
}