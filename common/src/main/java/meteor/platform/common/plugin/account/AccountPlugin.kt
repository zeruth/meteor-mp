package meteor.platform.common.plugin.account

import client.events.ResetCredentials
import meteor.platform.common.Common.clientInstance
import meteor.platform.common.events.ConfigChanged
import meteor.platform.common.plugin.Plugin
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