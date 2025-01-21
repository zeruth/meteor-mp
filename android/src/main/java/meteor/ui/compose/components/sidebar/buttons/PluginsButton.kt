package meteor.ui.compose.components.sidebar.buttons

import androidx.compose.runtime.mutableStateMapOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.PlugSolid
import meteor.common.panel.PanelComposables
import meteor.common.plugin.Plugin
import meteor.common.plugin.PluginsComposables.PluginList
import meteor.ui.compose.components.sidebar.SidebarButton

class PluginsButton : SidebarButton(icon = LineAwesomeIcons.PlugSolid) {
    companion object {
        val favoritesMap = mutableStateMapOf<Plugin, Boolean>()
        val switchStateMap = mutableStateMapOf<String, Boolean>()
        val textStateMap = mutableStateMapOf<String, String>()
    }

    override fun onClick() {
        PanelComposables.content.value = PluginList()
    }
}