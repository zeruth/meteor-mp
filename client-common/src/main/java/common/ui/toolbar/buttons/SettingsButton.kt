package common.ui.toolbar.buttons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import common.ui.toolbar.ToolbarButton
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CogSolid

class SettingsButton : ToolbarButton() {
    override var icon: MutableState<ImageVector?> = mutableStateOf(LineAwesomeIcons.CogSolid)

    @Composable
    override fun drawerContent() {
        Text("Settings")
    }
}