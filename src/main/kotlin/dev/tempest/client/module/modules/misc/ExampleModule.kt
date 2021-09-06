package dev.tempest.client.module.modules.misc

import dev.tempest.api.setting.SettingManager
import dev.tempest.api.setting.values.BooleanSetting
import dev.tempest.api.setting.values.DoubleSetting
import dev.tempest.api.setting.values.IntegerSetting
import dev.tempest.api.setting.values.ModeSetting
import dev.tempest.api.util.MessageUtil
import dev.tempest.client.module.Module
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