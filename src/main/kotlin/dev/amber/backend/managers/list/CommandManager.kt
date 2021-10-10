package dev.amber.backend.managers.list

import dev.amber.api.util.LOGGER
import dev.amber.backend.events.core.EventTarget
import dev.amber.backend.events.core.imp.Priority
import dev.amber.backend.events.list.EventMessage
import dev.amber.frontend.command.Command
import dev.amber.client.command.commands.TestCommand

/*
    @author TechAle
    @since 07/09/21
 */
object CommandManager : manager {
    val commands = arrayListOf<Command>()
    const val prefix = "-"

    override fun onLoad() {
        LOGGER.startTimer("Command Manager")
        addCommand(TestCommand)
        LOGGER.endTimer("Command Manager")
    }

    private fun addCommand(c : Command) {
        commands.add(c)
    }

    @EventTarget(Priority.HIGHEST)
    fun prova(event : EventMessage) {
        val msg = event.message.message

        if (msg.startsWith(prefix)) {
            event.message.isCanceled = true

            val command = msg.split(" ")[0].drop(1)
            var found = false

            commands.forEach {
                if (it.aliasList.contains(command)) {
                    it.onCommand(msg.split(" ").drop(1))
                    found = true
                    return@forEach
                }
            }
            // If not found
            if (!found) {
                // Message help
                println("Nothing found")
            }
        }
    }

}