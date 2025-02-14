package meteor.platform.common.plugin.meteor

import meteor.platform.common.config.Config
import meteor.platform.common.config.ConfigItem
import meteor.plugin.meteor.FilterQuality

class MeteorConfig(plugin: MeteorPlugin) : Config(plugin) {
    val uiColor = ConfigItem(this, "UI color", "uicolor".key(), UIColor.WHITE)
    val filterQuality = ConfigItem(this, "Filter quality", "filterQuality".key(),
        FilterQuality.None
    )
}