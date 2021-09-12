package dev.amber.api.util

import com.mojang.realmsclient.gui.ChatFormatting
import dev.amber.api.util.Globals.mc
import net.minecraft.util.text.TextComponentString

/**
 * @author A2H
 * @author Hoosiers
 */
object MessageUtil {

    private var watermark = "${ChatFormatting.GRAY}[${ChatFormatting.LIGHT_PURPLE}Amber${ChatFormatting.GRAY}]${ChatFormatting.RESET}"

    fun sendClientMessage(message: String) {
        mc.player.sendMessage(TextComponentString("$watermark $message"))
    }

    fun sendServerMessage(message: String) {
        mc.player.sendChatMessage(message)
    }
}