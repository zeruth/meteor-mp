package meteor.common.config

import meteor.common.plugin.Plugin

open class Config(var plugin: Plugin? = null) {
    //items helps maintain member order after compiling (kotlin will alphabetize members during compiling)
    val items = ArrayList<ConfigItem<*>>()
    fun String.key(): String {
        return "${plugin!!.name}.$this"
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): ConfigItem<T> {
        return items.first { it.key == key } as ConfigItem<T>
    }

    fun add(item: ConfigItem<*>) {
        items.add(item)
    }
}