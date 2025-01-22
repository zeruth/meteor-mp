package meteor.common

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import com.google.gson.GsonBuilder
import meteor.common.plugin.Plugin
import meteor.common.ui.components.sidebar.UISide
import kotlin.properties.Delegates

object Common {
    var isAndroid by Delegates.notNull<Boolean>()
    val gson = GsonBuilder().setPrettyPrinting().create()

    //todo: move somewhere better
    val filterQuality = mutableStateOf(meteor.plugin.meteor.FilterQuality.None)
    val favoritesMap = mutableStateMapOf<Plugin, Boolean>()
    val switchStateMap = mutableStateMapOf<String, Boolean>()
    val textStateMap = mutableStateMapOf<String, String>()
    val sidebarWidth = mutableStateOf(40.dp)
    val configWidth = mutableStateOf(300.dp)
    var panelOpen = mutableStateOf(false)
    val uiSide = mutableStateOf(UISide.RIGHT)
}