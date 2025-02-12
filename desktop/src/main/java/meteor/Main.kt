package meteor

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import client.events.LoggerMessage
import jagex2.client.WebSocketProxy
import meteor.audio.MidiPlayer
import meteor.audio.SoundPlayer
import meteor.common.Common
import meteor.common.Common.logger
import meteor.common.Configuration
import meteor.common.config.ConfigManager
import meteor.common.plugin.PluginManager
import meteor.discord.DiscordPlugin
import meteor.discord.DiscordPresence
import meteor.discord.DiscordPresence.updatingDiscordState
import meteor.ui.MeteorWindow.MeteorWindow
import meteor.ui.buttons.DiscordStatusButton.Companion.showDiscordStatusWindow
import org.rationalityfrontline.kevent.KEVENT
import java.io.File

object Main {
    private val startupTime = System.currentTimeMillis()
    private var started = false

    private val volatileLogger = Logger("")

    init {
        KEVENT.subscribe<LoggerMessage> {
            volatileLogger.name = it.data.header
            volatileLogger.debug(it.data.message)
        }
        Common.isAndroid = false
        Logger.logFile = File(Configuration.dataDir, "log.txt")
        MidiPlayer.init()
        SoundPlayer.init()
        Game.init()
        PluginManager.addAll(DiscordPlugin())
        PluginManager.start()

        val discordPlugin = PluginManager.get<DiscordPlugin>()!!
        if (discordPlugin.enabled()) {
            val lastPresence = ConfigManager.get<String>("DiscordRPCStatus", "")
            DiscordPresence.update(lastPresence)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = application {
        MeteorWindow()

        key(updatingDiscordState.value) {
            if (updatingDiscordState.value)
                showDiscordStatusWindow()
        }

        if (!started) {
            val gameInit = System.currentTimeMillis() - startupTime
            logger.info("Game init: ${gameInit}ms")

            LaunchedEffect(Unit) {
                val composeInit = System.currentTimeMillis() - startupTime
                logger.info("Compose init: ${composeInit}ms")
            }
            started = true
        }
    }
}