package dev.tempest.backend.managers.list

import dev.amber.client.command.Command
import dev.amber.client.command.commands.TestCommand
import dev.tempest.api.util.timer
import net.minecraftforge.client.event.ClientChatEvent

/*
    @author TechAle
    @since 07/09/21
 */
object CommandManager : manager {
    val commands = arrayListOf<Command>()
    const val prefix = "-"

    override fun onLoad() {
        val count = timer();
        addCommand(TestCommand)
        count.endTimer("Manager Command")
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