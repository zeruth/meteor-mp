package meteor.plugin.infobars

import meteor.config.Config
import meteor.config.ConfigItem
import meteor.plugin.Plugin

class InfoBarsConfig(plugin: Plugin) : Config(plugin) {
    val skillTimeout = ConfigItem(this, "Skill timeout (minutes)", "skillTimeout".key(), 1)
    val ignoreFirstUpdate = ConfigItem(this, "Ignore first update", "ignoreFirstUpdate".key(), true)
    val hideWhenInterfaceOpen = ConfigItem(this, "Hide when interface open", "hideWhenInterfaceOpen".key(), true)
    val longerBars = ConfigItem(this, "Longer bars", "longerBars".key(), false)
}