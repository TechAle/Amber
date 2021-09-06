package dev.amber.api.setting.values

import dev.amber.api.setting.Setting
import java.lang.Integer.min
import java.lang.Integer.max

/**
 * @author lukflug
 */
class StringSetting(
    displayName: String,
    value: String,
    configName: String = displayName.replace(" ",""),
    description: String = "",
    visible: ()-> Boolean = {true}
): Setting<String>(displayName, value, configName, description, visible) {

    override fun setState(value: String) {
        super.setState(value)
    }

    override fun fromString(input: String) {
        setState(input)
    }
}