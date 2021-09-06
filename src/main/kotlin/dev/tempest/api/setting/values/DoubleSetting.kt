package dev.tempest.api.setting.values

import dev.tempest.api.setting.Setting
import java.lang.Double.min
import java.lang.Double.max

/**
 * @author lukflug
 */
class DoubleSetting(
    displayName: String,
    value: Double,
    val min: Double,
    val max: Double,
    configName: String = displayName.replace(" ",""),
    description: String = "",
    visible: ()-> Boolean = {true}
): Setting<Double>(displayName, value, configName, description,visible) {

    override fun setState(value: Double) {
        super.setState(max(min(value, max(min, max)), min(min, max)))
    }

    override fun fromString(input: String) {
        setState(input.toDouble())
    }
}