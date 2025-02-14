package meteor.platform.common.plugin.account

import meteor.platform.common.config.Config
import meteor.platform.common.config.ConfigItem

class AccountConfig(plugin: AccountPlugin) : Config(plugin) {
    val username = ConfigItem(this, "Username", "user".key(), "")
    val password = ConfigItem(this, "Password", "pass".key(), "", secret = true)
}