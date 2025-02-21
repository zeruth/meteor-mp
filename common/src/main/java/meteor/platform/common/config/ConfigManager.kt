package meteor.platform.common.config

import meteor.logger.Logger
import meteor.platform.common.Common.gson
import meteor.platform.common.Configuration
import meteor.platform.common.Common
import meteor.platform.common.Common.eventbus
import meteor.platform.common.Common.startupTime
import meteor.platform.common.events.ConfigChanged
import java.io.File

object ConfigManager {
    val logger = Logger("config")
    val configFile = File(Configuration.dataDir, "properties")
    var properties = Properties()
    val configItems = ArrayList<ConfigItem<*>>()

    init {
        if (configFile.exists()) {
            properties = gson.fromJson(configFile.reader(), Properties::class.java)
            val configsLoaded = System.currentTimeMillis() - startupTime
            Common.logger.info("ConfigManager init @ ${configsLoaded}ms")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getItem(key: String) : ConfigItem<T>? {
        return getGeneric(key) as ConfigItem<T>?
    }

    fun getGeneric(key: String) : ConfigItem<*>? {
        return configItems.firstOrNull { it.key == key }
    }

    fun getString(key: String, defaultValue: Any): String {
        val currentValue = properties.properties[key] ?: return defaultValue.toString()
        return currentValue
    }

    inline fun <reified T> get(key: String, defaultValue: Any): T {
        val value = properties.properties[key]
        try {
            if (T::class == String::class) {
                if (value == null)
                    return defaultValue as T
                return value as T
            }
            return gson.fromJson(value, T::class.java)?: return defaultValue as T
        } catch (e: Exception) {
            logger.error("Error parsing config value $value for key $key")
            logger.error(e)
            return defaultValue as T
        }
    }

    fun <T> updateValue(key: String, value: T): Boolean {
        //No serialization required
        if (value is String) {
            if (properties.properties[key] != value) {
                properties.properties[key] = value
                return true
            }
        }
        //serialize
        val json = gson.toJson(value)
        if (properties.properties[key] != json) {
            properties.properties[key] = json
            return true
        }
        return false
    }

    /**
     * Only post/save if the new value != the old value
     */
    fun <T> set(key: String, value: T) {
        if (updateValue(key, value)) {
            eventbus.post(ConfigChanged(getItem<T>(key)))
            save()
        }
    }

    fun save() {
        //Don't save empty properties ever.
        if (properties.properties.isNotEmpty())
            configFile.writeText(gson.toJson(properties))
    }
}