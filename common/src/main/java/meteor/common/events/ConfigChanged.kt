package meteor.common.events

import meteor.common.config.Config
import meteor.common.config.ConfigItem


class ConfigChanged(val item: ConfigItem<*>?) {
    fun affects(config: Config): Boolean {
        return config.items.contains(item)
    }
}