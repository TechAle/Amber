package dev.amber.api.setting

import dev.amber.client.module.Module
import java.util.stream.Stream

/**
 * @author lukflug
 */
object SettingManager {
    private val settings: MutableList<Pair<Module, Setting<*>>> = ArrayList()

	fun registerSetting(setting: Setting<*>, mod: Module): Setting<*> {
        settings.add(Pair(mod,setting))
        return setting
    }

    fun getSettings(): Stream<Setting<*>> =
            settings.stream().map{s -> s.second}

    fun getSettingByNameAndMod(name: String, mod: Module): Setting<*> =
            settings.stream().filter{s -> s.first == mod && s.second.displayName == name}.map{s -> s.second}.findFirst().orElse(null)

    fun getSettingsByModule(mod: Module): Stream<Setting<*>> =
            settings.stream().filter{s -> s.first == mod}.map{s -> s.second}
}
