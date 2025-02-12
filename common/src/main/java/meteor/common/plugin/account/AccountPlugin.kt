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
            updateCredentials()
        }
    }

    override fun onStart() {
        updateCredentials()
    }

    override fun onConfigChanged(it: ConfigChanged) {
        if (it.affects(config)) {
            var update = false
            if (it.item == config.username) {
                update = true
            }
            if (it.item == config.password) {
                update = true
            }
            if (update)
                updateCredentials()
        }
    }

    fun updateCredentials() {
        clientInstance.username = config.username.get()
        clientInstance.password = config.password.get()
    }
}