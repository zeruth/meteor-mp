package meteor.ui.buttons

import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CompressArrowsAltSolid
import compose.icons.lineawesomeicons.ExpandArrowsAltSolid
import meteor.common.config.ConfigManager
import meteor.common.ui.components.sidebar.SidebarButton
import meteor.ui.GameView

class FullscreenToggleButton : SidebarButton(
    icon = if (ConfigManager.get("meteor.fullscreen", false)) LineAwesomeIcons.CompressArrowsAltSolid else LineAwesomeIcons.ExpandArrowsAltSolid,
    actionButton = true,
    bottom = true) {
    override fun onClick() {
        GameView.toggleFullscreen()
    }
}