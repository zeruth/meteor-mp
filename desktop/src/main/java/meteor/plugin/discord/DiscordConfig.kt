package meteor.plugin.discord

import meteor.common.config.Config
import meteor.common.config.ConfigItem
import meteor.common.plugin.Plugin


class DiscordConfig(plugin: Plugin) : Config(plugin) {
    val enabled = ConfigItem(this, "Enabled", "enabled".key(), true)
    val sendStatusLoggedOut = ConfigItem(this, "Send status logged out", "sendStatusLoggedOut".key(), true)
}