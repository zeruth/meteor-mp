package meteor.platform.desktop.ui.buttons

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.WindowCloseSolid
import meteor.platform.common.ui.components.sidebar.SidebarButton
import kotlin.system.exitProcess

class CloseMeteorButton : SidebarButton(
    icon = mutableStateOf(LineAwesomeIcons.WindowCloseSolid),
    actionButton = true,
    tint = Color.Red,
    bottom = true, position = Int.MAX_VALUE) {
    override fun onClick() {
        exitProcess(0)
    }
}