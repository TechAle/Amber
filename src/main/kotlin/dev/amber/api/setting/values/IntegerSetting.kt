package dev.amber.api.setting.values

import dev.amber.api.setting.Setting
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * @author lukflug
 */
class IntegerSetting(
    displayName: String,
    value: Int,
    val min: Int,
    val max: Int,
    configName: String = displayName.replace(" ",""),
    description: String = "",
    visible: ()-> Boolean = {true}
): Setting<Int>(displayName, value, configName, description, visible) {

    override fun setState(value: Int) {
        super.setState(max(min(value, max(min,max)), min(min,max)))
    }

    override fun fromString(input: String) {
        setState(input.toInt())
    }
}