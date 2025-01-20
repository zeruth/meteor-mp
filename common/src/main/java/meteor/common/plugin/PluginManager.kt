package meteor.common.plugin

import androidx.compose.runtime.mutableStateMapOf
import meteor.Logger
//import meteor.plugin.discord.DiscordPlugin
import meteor.common.plugin.meteor.MeteorPlugin

object PluginManager {
    val plugins = mutableListOf<Plugin>()
    val logger = Logger("PluginManager")
    val runningMap = mutableStateMapOf<Plugin, Boolean>()

    init {
        //plugins.add(DiscordPlugin())
        plugins.add(MeteorPlugin())
    }

    fun startPlugins() {
        val startTime = System.currentTimeMillis()
        for (plugin in plugins) {
            plugin.start()
        }
        logger.info("Loaded ${plugins.size} plugins (${System.currentTimeMillis() - startTime}ms)")
    }

    inline fun <reified P : Plugin> get(): P? {
        return plugins.filterIsInstance<P>().firstOrNull()
    }
}