package meteor.plugin.filtering

import androidx.compose.ui.graphics.FilterQuality
import meteor.config.Config
import meteor.config.ConfigItem
import meteor.plugin.Plugin

class FilteringConfig(plugin: Plugin) : Config(plugin) {
    val quality = ConfigItem(this, "Quality", "quality".key(), Quality.NEAREST_NEIGHBOR)
}