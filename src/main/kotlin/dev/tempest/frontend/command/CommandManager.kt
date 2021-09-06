package dev.amber.client.command

import dev.amber.client.command.commands.TestCommand
import net.minecraftforge.client.event.ClientChatEvent

/**
 * @author Techale
 */
object CommandManager {
    val commands = arrayListOf<Command>()
    const val prefix = "-"

    fun registerCommands() {
        addCommand(TestCommand)
    }

    private fun addCommand(c : Command) {
        commands.add(c)
    }

    fun onMessage(event: ClientChatEvent) {
        val msg = event.message

        if (msg.startsWith(prefix)) {
            event.isCanceled = true

            val command = msg.split(" ")[0].drop(1)
            var found = true

            commands.forEach {
                if (it.aliasList.contains(command)) {
                    it.onCommand(msg.split(" ").drop(1))
                    found = false
                    return@forEach
                }
            }
            // If not found
            if (found) {

            }
        }
    }
}