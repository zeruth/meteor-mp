package meteor

import androidx.compose.runtime.mutableStateOf
import com.google.gson.GsonBuilder
import meteor.common.config.ConfigManager
import kotlin.properties.Delegates

object Common {
    var isAndroid by Delegates.notNull<Boolean>()
    val gson = GsonBuilder().setPrettyPrinting().create()

    //todo: move somewhere better
    val filterQuality = mutableStateOf(meteor.plugin.meteor.FilterQuality.None)
}