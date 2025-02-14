package meteor.platform.common.events

import meteor.platform.common.config.Config
import meteor.platform.common.config.ConfigItem


class ConfigChanged(val item: ConfigItem<*>?) {
    fun affects(config: Config): Boolean {
        return config.items.contains(item)
    }
}