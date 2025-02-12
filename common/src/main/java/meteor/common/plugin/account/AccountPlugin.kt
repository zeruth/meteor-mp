package meteor.common.plugin.account

import client.events.ResetCredentials
import meteor.common.Common.clientInstance
import meteor.common.config.ConfigManager
import meteor.common.events.ConfigChanged
import meteor.common.plugin.Plugin
import meteor.common.plugin.meteor.UIColor
import meteor.common.ui.Colors
import meteor.common.ui.UI.filterQuality
import org.rationalityfrontline.kevent.KEVENT

class AccountPlugin : Plugin("Account", cantDisable = true, enabledByDefault = true) {
    val config = configuration<AccountConfig>()

    init {
        KEVENT.subscribe<ResetCredentials> {
            clientInstance.username = config.username.get()
            clientInstance.password = config.password.get()
        }
    }

    override fun onConfigChanged(it: ConfigChanged) {
        if (it.affects(config)) {
            if (it.item == config.username) {
                clientInstance.username = config.username.get()
            }
            if (it.item == config.password) {
                clientInstance.password = config.password.get()
            }
        }
    }
}