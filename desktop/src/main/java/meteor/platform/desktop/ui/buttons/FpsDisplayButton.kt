package meteor.platform.desktop.ui.buttons

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Android
import meteor.platform.common.ui.components.sidebar.SidebarButton
import meteor.platform.desktop.ui.GameView.fps

class FpsDisplayButton: SidebarButton(icon = mutableStateOf(LineAwesomeIcons.Android), bottom = true, actionButton = true) {
    override fun content(): @Composable (BoxScope.() -> Unit) = @Composable {
        Column(Modifier.align(Alignment.Center)) {
            Row(modifier = Modifier.height(10.dp).align(Alignment.CenterHorizontally)) {
                Column {
                    Text("FPS", fontSize = 9.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
            Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally)) {
                Column {
                    Text("${fps.intValue}", fontSize = 17.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}