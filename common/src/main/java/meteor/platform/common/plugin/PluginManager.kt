package meteor.platform.common.plugin

import androidx.compose.runtime.mutableStateMapOf
import meteor.Logger
import meteor.platform.common.plugin.account.AccountPlugin
//import meteor.plugin.discord.DiscordPlugin
import meteor.platform.common.plugin.meteor.MeteorPlugin

object PluginManager {
    val plugins = mutableListOf<Plugin>()
    val logger = Logger("PluginManager")
    val runningMap = mutableStateMapOf<Plugin, Boolean>()

    init {
        plugins.add(MeteorPlugin())
        plugins.add(AccountPlugin())
    }

    fun addAll(vararg plugins: Plugin) {
        plugins.forEach {
            PluginManager.plugins.add(it)
        }
    }

    fun start() {
        for (plugin in plugins) {
            plugin.start()
        }
    }

    inline fun <reified P : Plugin> get(): P? {
        return plugins.filterIsInstance<P>().firstOrNull()
    }
}