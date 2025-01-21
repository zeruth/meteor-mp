package meteor

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.GsonBuilder
import meteor.common.config.ConfigManager
import meteor.common.plugin.Plugin
import kotlin.properties.Delegates

object Common {
    var isAndroid by Delegates.notNull<Boolean>()
    val gson = GsonBuilder().setPrettyPrinting().create()

    //todo: move somewhere better
    val filterQuality = mutableStateOf(meteor.plugin.meteor.FilterQuality.None)
    val favoritesMap = mutableStateMapOf<Plugin, Boolean>()
    val switchStateMap = mutableStateMapOf<String, Boolean>()
    val textStateMap = mutableStateMapOf<String, String>()
}