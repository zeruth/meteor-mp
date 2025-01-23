package meteor

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import client.client
import com.google.gson.GsonBuilder
import meteor.discord.DiscordPresence.updatingDiscordState
import meteor.audio.MidiPlayer
import meteor.audio.SoundPlayer
import meteor.common.Common
import meteor.common.Configuration
import meteor.common.config.ConfigManager
import meteor.common.plugin.PluginManager
import meteor.discord.DiscordPresence
import meteor.ui.MeteorWindow.MeteorWindow
import meteor.ui.buttons.DiscordStatusButton.Companion.showDiscordStatusWindow
import java.io.File

object Main {
    lateinit var client: client
    val gson = GsonBuilder().setPrettyPrinting().create()
    val logger = Logger("main")
    private val startupTime = System.currentTimeMillis()
    private var started = false

    init {
        Common.isAndroid = false
        Logger.logFile = File(Configuration.dataDir, "log.txt")
        MidiPlayer.init()
        SoundPlayer.init()
        Game.init()
        PluginManager.start()

        val lastPresence = ConfigManager.get<String>("DiscordRPCStatus", "")
        DiscordPresence.update(lastPresence)
    }

    @JvmStatic
    fun main(args: Array<String>) = application {
        MeteorWindow()

        key(updatingDiscordState.value) {
            if (updatingDiscordState.value)
                showDiscordStatusWindow()
        }

        if (!started) {
            LaunchedEffect(Unit) {
                val startupDuration = System.currentTimeMillis() - startupTime
                logger.info("Startup time: ${startupDuration}ms")
            }
            started = true
        }
    }
}