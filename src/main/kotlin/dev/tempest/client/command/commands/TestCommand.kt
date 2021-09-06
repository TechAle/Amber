package dev.tempest.client.command.commands

import dev.tempest.client.command.Command

object TestCommand : Command(name = "TestCommand", "Syntax", "tst", "gg") {

    override fun onCommand(Options: List<String>?) {

    }
}