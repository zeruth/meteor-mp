package meteor.platform.desktop.discord

import meteor.platform.common.config.Config
import meteor.platform.common.config.ConfigItem
import meteor.platform.common.plugin.Plugin


class DiscordConfig(plugin: Plugin) : Config(plugin) {
    val enabled = ConfigItem(this, "Enabled", "enabled".key(), true)
    val sendStatusLoggedOut = ConfigItem(this, "Send status logged out", "sendStatusLoggedOut".key(), true)
}