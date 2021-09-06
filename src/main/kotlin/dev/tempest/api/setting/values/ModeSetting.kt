package dev.tempest.api.setting.values

import dev.tempest.api.setting.Setting

/**
 * @author lukflug
 */
class ModeSetting<E: Enum<E>>(
    displayName: String,
    value: E,
    configName: String = displayName.replace(" ",""),
    description: String = "",
    visible: ()-> Boolean = {true}
): Setting<E>(displayName, value, configName, description, visible) {

    fun increment() {
        val values: Array<E> = getState().declaringClass.enumConstants
        var index: Int = getState().ordinal+1
        if (index>values.size) index=0
        setState(values[index])
    }

    fun decrement() {
        val values: Array<E> = getState().declaringClass.enumConstants
        var index: Int = getState().ordinal-1
        if (index<0) index = values.size-1
        setState(values[index])
    }

    override fun fromString(input: String) {
        for (candidate in getState().declaringClass.enumConstants) {
            if (candidate.toString().equals(input, true)) {
                setState(candidate)
                return
            }
        }
    }
}