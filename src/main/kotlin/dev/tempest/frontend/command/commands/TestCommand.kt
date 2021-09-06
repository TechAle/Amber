package dev.amber.client.command.commands

import dev.amber.client.command.Command

object TestCommand : Command(name = "TestCommand", "Syntax", "tst", "gg") {

    override fun onCommand(Options: List<String>?) {

    }
}