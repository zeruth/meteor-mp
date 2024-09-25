package meteor.plugin.meteor

import meteor.events.client.ConfigChanged
import meteor.plugin.Plugin
import meteor.ui.compose.Colors

class MeteorPlugin : Plugin("Meteor", cantDisable = true, enabledByDefault = true) {
    val config = configuration<MeteorConfig>()
    override fun onStart() {
        Colors.secondary.value = config.uiColor.get<UIColor>().color
    }

    override fun onConfigChanged(it: ConfigChanged) {
        if (it.affects(config)) {
            if (it.item == config.uiColor) {
                Colors.secondary.value = config.uiColor.get<UIColor>().color
            }
        }
    }
}