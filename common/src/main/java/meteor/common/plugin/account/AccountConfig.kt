package meteor.common.plugin.account

import meteor.common.config.Config
import meteor.common.config.ConfigItem
import meteor.plugin.meteor.FilterQuality

class AccountConfig(plugin: AccountPlugin) : Config(plugin) {
    val username = ConfigItem(this, "Username", "user".key(), "")
    val password = ConfigItem(this, "Password", "pass".key(), "", secret = true)
}