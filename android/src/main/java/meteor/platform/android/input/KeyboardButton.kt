package meteor.platform.android.input

import androidx.compose.runtime.mutableStateOf
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.KeyboardSolid
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.android.input.KeyboardController.keyboardController
import meteor.platform.android.ui.GamePanel.gamePanelFocusRequester

class KeyboardButton: SidebarButton(icon = mutableStateOf(LineAwesomeIcons.KeyboardSolid), actionButton = true, bottom = true) {
    override fun onClick() {
        gamePanelFocusRequester.requestFocus()
        keyboardController.show()
    }
}