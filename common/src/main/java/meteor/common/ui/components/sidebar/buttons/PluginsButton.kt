package meteor.common.ui.components.sidebar.buttons

import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.PlugSolid
import meteor.common.panel.PanelComposables
import meteor.common.plugin.PluginsComposables.PluginList
import meteor.common.ui.components.sidebar.SidebarButton

class PluginsButton : SidebarButton(
    icon = LineAwesomeIcons.PlugSolid,
    position = Int.MIN_VALUE) {
    override fun onClick() {
        PanelComposables.content.value = PluginList()
    }
}