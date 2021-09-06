package dev.amber.api.setting

/**
 * @author lukflug
 */
abstract class Setting<T>(
    val displayName: String,
    private var value: T,
    val configName: String = displayName.replace(" ",""),
    val description: String = "",
    val visible: ()-> Boolean = {true}
) {
    open fun getState(): T = value
    open fun setState(value: T) {this.value = value}
    open fun getString(): String = value.toString()
    abstract fun fromString (input: String)
}