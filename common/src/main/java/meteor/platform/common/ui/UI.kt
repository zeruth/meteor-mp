package meteor.platform.common.ui

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import meteor.platform.common.plugin.Plugin
import meteor.platform.common.ui.components.sidebar.UISide

object UI {
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