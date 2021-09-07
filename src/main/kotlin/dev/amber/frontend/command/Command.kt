package dev.amber.client.command

/*
    @author TechAle
    @since 07/09/21
 */
abstract class Command(val name: String, val syntax: String, vararg alias: String) {

    val aliasList: List<String> = alias.toList()

    open fun onCommand(Options: List<String>?) {}
}