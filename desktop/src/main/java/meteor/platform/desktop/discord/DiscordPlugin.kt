package meteor.platform.desktop.discord

import client.events.InGameChanged
import meteor.platform.common.plugin.Plugin
import org.rationalityfrontline.kevent.KEVENT


class DiscordPlugin : Plugin("Discord", true, cantDisable = true) {
    val config = configuration<DiscordConfig>()

    init {
        KEVENT.subscribe<InGameChanged> {
            DiscordPresence.refresh()
        }
    }

    fun enabled() : Boolean {
        return config.enabled.get<Boolean>()
    }

    fun sendStatusLoggedOut() : Boolean {
        return config.sendStatusLoggedOut.get<Boolean>()
    }
}