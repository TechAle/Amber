package dev.tempest.client.command

/**
 * @author Techale
 */
open class Command(val name: String, val syntax: String, vararg alias: String) {

    val aliasList: List<String> = alias.toList()

    open fun onCommand(Options: List<String>?) {}
}