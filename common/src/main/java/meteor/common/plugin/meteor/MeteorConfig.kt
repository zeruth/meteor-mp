package meteor.common.plugin.meteor

import meteor.common.config.Config
import meteor.common.config.ConfigItem
import meteor.plugin.meteor.FilterQuality

class MeteorConfig(plugin: MeteorPlugin) : Config(plugin) {
    val uiColor = ConfigItem(this, "UI color", "uicolor".key(), UIColor.GREEN)
    val filterQuality = ConfigItem(this, "Filter quality", "filterQuality".key(),
        FilterQuality.None
    )
    val test2 = ConfigItem(this, "1", "1".key(), false)
    val test1 = ConfigItem(this, "2", "2".key(), "")
    val testStr = ConfigItem(this, "3", "3".key(), 123321)
}