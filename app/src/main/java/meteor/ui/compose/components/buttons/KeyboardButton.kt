package meteor.ui.compose.components.buttons

import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.KeyboardSolid
import meteor.Main
import meteor.events.client.KeyboardButtonEvent
import meteor.ui.compose.components.sidebar.SidebarButton

class KeyboardButton : SidebarButton(icon = LineAwesomeIcons.KeyboardSolid, bottom = true, actionButton = true) {
    override fun onClick() {
        Main.client.callbacks.post(KeyboardButtonEvent)
    }
}