package meteor.ui.buttons

import androidx.compose.ui.graphics.Color
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.WindowCloseSolid
import meteor.common.ui.components.sidebar.SidebarButton
import kotlin.system.exitProcess

class CloseMeteorButton : SidebarButton(
    icon = LineAwesomeIcons.WindowCloseSolid,
    actionButton = true,
    tint = Color.Red,
    bottom = true, position = Int.MAX_VALUE) {
    override fun onClick() {
        exitProcess(0)
    }
}