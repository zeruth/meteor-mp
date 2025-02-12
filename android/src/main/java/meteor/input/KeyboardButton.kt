package meteor.input

import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.KeyboardSolid
import meteor.common.ui.components.sidebar.SidebarButton
import meteor.input.KeyboardController.keyboardController
import meteor.ui.GamePanel.gamePanelFocusRequester

class KeyboardButton: SidebarButton(icon = LineAwesomeIcons.KeyboardSolid, actionButton = true, bottom = true) {
    override fun onClick() {
        gamePanelFocusRequester.requestFocus()
        keyboardController.show()
    }
}