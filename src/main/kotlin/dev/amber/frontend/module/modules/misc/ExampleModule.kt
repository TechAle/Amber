package dev.amber.client.module.modules.misc

import dev.amber.api.setting.SettingManager
import dev.amber.api.setting.values.BooleanSetting
import dev.amber.api.setting.values.DoubleSetting
import dev.amber.api.setting.values.IntegerSetting
import dev.amber.api.setting.values.ModeSetting
import dev.amber.api.util.MessageUtil
import dev.amber.client.module.Module
import org.lwjgl.input.Keyboard

/**
 * @Author A2H
 */
object ExampleModule : Module(category = Category.Misc, "Example") {
    override var key = Keyboard.KEY_APOSTROPHE
    val settingA=SettingManager.registerSetting(BooleanSetting("Boolean Thingy",false),this)
    val settingB=SettingManager.registerSetting(DoubleSetting("Double Thingy",1.0,0.0,10.0),this)
    val settingC=SettingManager.registerSetting(IntegerSetting("Integer Thingy",25,0,100),this)
    val settingD=SettingManager.registerSetting(ModeSetting("Mode",Mode.A),this)

    override fun onEnable() {
        MessageUtil.sendClientMessage("Example Module")
        toggle()
    }

    enum class Mode {
        A,B,C;
    }
}