package meteor.common.ui.components.sidebar.buttons

import androidx.compose.runtime.mutableStateOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.PlugSolid
import meteor.common.ui.components.panel.PanelComposables
import meteor.common.ui.components.plugin.PluginsComposables

import meteor.common.ui.components.sidebar.SidebarButton

class PluginsButton : SidebarButton(
    icon = mutableStateOf(LineAwesomeIcons.PlugSolid),
    position = Int.MIN_VALUE) {
    override fun onClick() {
        PanelComposables.content.value = PluginsComposables.PluginList()
    }
}