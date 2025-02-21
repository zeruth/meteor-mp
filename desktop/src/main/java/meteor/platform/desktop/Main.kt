package meteor.platform.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import client.events.LoggerMessage
import meteor.logger.Logger
import meteor.platform.desktop.audio.MidiPlayer
import meteor.platform.desktop.audio.SoundPlayer
import meteor.platform.common.Common
import meteor.platform.common.Common.logger
import meteor.platform.common.Common.startupTime
import meteor.platform.common.Configuration
import meteor.platform.common.config.ConfigManager
import meteor.platform.common.plugin.PluginManager
import meteor.platform.desktop.discord.DiscordPlugin
import meteor.platform.desktop.discord.DiscordPresence
import meteor.platform.desktop.discord.DiscordPresence.updatingDiscordState
import meteor.platform.desktop.ui.MapView
import meteor.platform.desktop.ui.MeteorWindow.MeteorWindow
import meteor.platform.desktop.ui.buttons.DiscordStatusButton.Companion.showDiscordStatusWindow
import org.rationalityfrontline.kevent.KEVENT
import java.io.File

object Main {
    private var started = false

    private val volatileLogger = Logger()

    init {
        KEVENT.subscribe<LoggerMessage> {
            volatileLogger.scope = it.data.header
            volatileLogger.debug(it.data.message)
        }
        Common.isAndroid = false
/*        Logger.logFile = File(Configuration.dataDir, "log.txt")*/
        MidiPlayer.init()
        SoundPlayer.init()
        Game.init()
        val gameInit = System.currentTimeMillis() - startupTime
        logger.info("Game init @ ${gameInit}ms")
        PluginManager.addAll(DiscordPlugin())
        PluginManager.start()

        val discordPlugin = PluginManager.get<DiscordPlugin>()!!
        if (discordPlugin.enabled()) {
            val lastPresence = ConfigManager.get<String>("DiscordRPCStatus", "")
            DiscordPresence.update(lastPresence)
        }
        val pluginsInit = System.currentTimeMillis() - startupTime
        logger.info("PluginManager init @ ${pluginsInit}ms")
    }

    @JvmStatic
    fun main(args: Array<String>) = application {
        MeteorWindow()

        key(updatingDiscordState.value) {
            if (updatingDiscordState.value)
                showDiscordStatusWindow()
        }

        MapView.MapView()

        if (!started) {
            LaunchedEffect(Unit) {
                val composeInit = System.currentTimeMillis() - startupTime
                logger.info("Meteor started @ ${composeInit}ms")
            }
            started = true
        }
    }
}