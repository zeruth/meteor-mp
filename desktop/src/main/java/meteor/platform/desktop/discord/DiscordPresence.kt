package meteor.platform.desktop.discord

import androidx.compose.runtime.mutableStateOf
import io.github.vyfor.kpresence.RichClient
import io.github.vyfor.kpresence.logger.ILogger
import io.github.vyfor.kpresence.logger.LogLevel
import io.github.vyfor.kpresence.rpc.ActivityType
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.config.ConfigManager
import meteor.platform.common.plugin.PluginManager

object DiscordPresence {
    private val discordClient = RichClient(969159977463848960)

    var updatingDiscordState = mutableStateOf(false)

    init {
        //discordClient.logger = ILogger.default(level = LogLevel.ERROR)
        //discordClient.connect(shouldBlock = true)
    }

    fun refresh() {
        update(ConfigManager.get<String>("DiscordRPCStatus", ""))
    }

    fun update(
        type: ActivityType = ActivityType.GAME,
        details: String? = null,
        state: String? = null,) {
/*        discordClient.update {
            this.type = type
            this.details = details
            this.state = state

            timestamps {
                start = now()
            }

            button("Play 2004scape", "https://2004scape.org/")
        }*/
    }

    fun update(state: String?) {
        var state = state
        PluginManager.get<DiscordPlugin>()?.let {
            if (!it.enabled())
                return
            if (!clientInstance.inGame() && !it.sendStatusLoggedOut())
                state = null
        }
        val details = if (clientInstance.inGame()) "Logged in" else "Logged out"

        if (state == null || state!!.length < 2 || state!!.length > 128) {
            update(details = details, state = null)
            return
        }

        update(details = details, state = state)
    }
}