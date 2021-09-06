package dev.tempest.api.setting.values

import dev.tempest.api.setting.Setting

/**
 * @author lukflug
 */
class BooleanSetting(
        displayName: String,
        value: Boolean,
        configName: String = displayName.replace(" ",""),
        description: String = "",
        visible: ()-> Boolean = {true}
): Setting<Boolean>(displayName,value,configName,description,visible) {

    fun invert() {
        setState(!getState())
    }

    override fun fromString(input: String) {
        setState(input.toBoolean())
    }
}