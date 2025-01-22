package meteor.common.plugin.meteor

import meteor.common.Common.filterQuality
import meteor.common.config.ConfigManager
import meteor.common.events.ConfigChanged
import meteor.common.plugin.Plugin
import meteor.common.ui.Colors

class MeteorPlugin : Plugin("Meteor", cantDisable = true, enabledByDefault = true) {
    val config = configuration<MeteorConfig>()
    override fun onStart() {
        Colors.secondary.value = config.uiColor.get<UIColor>().color
        filterQuality.value = ConfigManager.get<meteor.plugin.meteor.FilterQuality>("Meteor.filterQuality", meteor.plugin.meteor.FilterQuality.None)
    }

    override fun onConfigChanged(it: ConfigChanged) {
        if (it.affects(config)) {
            if (it.item == config.uiColor) {
                Colors.secondary.value = config.uiColor.get<UIColor>().color
            }
            if (it.item == config.filterQuality) {
                filterQuality.value = config.filterQuality.get()
            }
        }
    }
}