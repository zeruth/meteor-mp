package meteor.common.plugin.meteor

import meteor.common.config.Config
import meteor.common.config.ConfigItem
import meteor.plugin.meteor.FilterQuality

class MeteorConfig(plugin: MeteorPlugin) : Config(plugin) {
    val uiColor = ConfigItem(this, "UI color", "uicolor".key(), UIColor.GREEN)
    val filterQuality = ConfigItem(this, "Filter quality", "filterQuality".key(),
        FilterQuality.None
    )
}