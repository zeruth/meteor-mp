package meteor.platform.desktop.ui.buttons

import androidx.compose.runtime.mutableStateOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CompressArrowsAltSolid
import compose.icons.lineawesomeicons.ExpandArrowsAltSolid
import meteor.platform.common.config.ConfigManager
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.desktop.ui.GameView

class FullscreenToggleButton : SidebarButton(
    icon = if (ConfigManager.get("meteor.fullscreen", false)) mutableStateOf(LineAwesomeIcons.CompressArrowsAltSolid) else mutableStateOf(LineAwesomeIcons.ExpandArrowsAltSolid),
    actionButton = true,
    bottom = true) {
    override fun onClick() {
        GameView.toggleFullscreen()
    }
}