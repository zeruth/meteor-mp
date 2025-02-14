package meteor.platform.desktop.ui.buttons

import androidx.compose.runtime.mutableStateOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.LockSolid
import compose.icons.lineawesomeicons.UnlockSolid
import meteor.platform.common.config.ConfigManager
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.desktop.ui.GameView.stretchedMode
import meteor.platform.desktop.ui.MeteorWindow
import meteor.platform.desktop.ui.MeteorWindow.fixedState
import meteor.platform.desktop.ui.MeteorWindow.fullscreenState
import meteor.platform.desktop.ui.MeteorWindow.resetWindowSize
import meteor.platform.desktop.ui.MeteorWindow.windowState

class StretchToggleButton : SidebarButton(
    icon = if (!fixedState.value || (stretchedMode.value && windowState.value != fullscreenState)) mutableStateOf(LineAwesomeIcons.LockSolid) else mutableStateOf(LineAwesomeIcons.UnlockSolid),
    actionButton = true,
    bottom = true) {
    override fun onClick() {
        toggleStretchedMode()
    }

    fun toggleStretchedMode() {
        if (windowState.value == MeteorWindow.floatingState) {
            if (!stretchedMode.value) {
                fixedState.value = false
                stretchedMode.value = true
                windowState.value = MeteorWindow.floatingState
                icon!!.value = LineAwesomeIcons.LockSolid
                ConfigManager.set("meteor.stretched", true)
            } else {
                stretchedMode.value = false
                fixedState.value = true
                icon!!.value = LineAwesomeIcons.UnlockSolid
                ConfigManager.set("meteor.stretched", false)
                resetWindowSize()
            }
        }
    }
}